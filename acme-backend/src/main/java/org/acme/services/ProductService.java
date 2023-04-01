package org.acme.services;

import jakarta.validation.*;
import org.acme.daos.DAOFactory;
import org.acme.daos.record.ProductLangRecordDAO;
import org.acme.daos.record.ProductRecordDAO;
import org.acme.daos.view.ProductViewDAO;
import org.acme.dtos.ProductDTO;
import org.acme.dtos.ProductLangDTO;
import org.acme.generated.AbstractDTO;
import org.acme.generated.testshop.tables.Product;
import org.acme.generated.testshop.tables.records.ProductLangRecord;
import org.acme.generated.testshop.tables.records.ProductRecord;
import org.acme.jooq.JooqContext;
import org.acme.jooq.JooqContextFactory;
import org.acme.util.exception.ValidationException;
import org.acme.util.query.QueryParameters;
import org.acme.util.request.RequestContext;
import org.jooq.exception.DataAccessException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.jboss.logging.Logger;

/**
 * ProductService
 */
@ApplicationScoped
public class ProductService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class);

    @Inject
    JooqContextFactory jooqContextFactory;

    @Inject
    DAOFactory daoFactory;

    @Inject
    Validator validator;


    public List<ProductDTO> query(final RequestContext requestContext, final QueryParameters queryParameters) {
        // we use the request-scoped dsl-context as source for the configuration of the dao.
        JooqContext jooqContext = jooqContextFactory.createJooqContext(requestContext);
        ProductViewDAO productViewDAO = daoFactory.createProductViewDAO(jooqContext);
        return productViewDAO.query(queryParameters);
    }

    public Optional<ProductDTO> getOne(final RequestContext requestContext, final Long productId) throws DataAccessException {
        JooqContext jooqContext = jooqContextFactory.createJooqContext(requestContext);
        ProductViewDAO productViewDAO = daoFactory.createProductViewDAO(jooqContext);
        return productViewDAO.findOptionalById(productId);
    }

    @Transactional
    public ProductDTO create(final RequestContext requestContext, final ProductDTO product) throws ValidationException {
        JooqContext jooqContext = jooqContextFactory.createJooqContext(requestContext);
        ProductRecordDAO productRecordDAO = daoFactory.createProductRecordDAO(jooqContext);
        ProductLangRecordDAO productLangRecordDAO = daoFactory.createProductLangRecordDAO(jooqContext);

        // Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
        // if (!violations.isEmpty()) {
        //     throw new ValidationException(violations);
        // }

        // we first use insertAndReturn() to insert the product and get the autoincrement-id for it
        // afterwards we use insert() to insert the productLanguages in the most performant (batching) way.
        productRecordDAO.insertAndReturnDTO(product);
        for (ProductLangDTO xLang : product.getLangs()) {
            xLang.setProductId(product.getProductId());
        }
        productLangRecordDAO.insertDTOs(product.getLangs());
        return product;
    }

    @Transactional
    public ProductDTO update(final RequestContext requestContext, final ProductDTO product) {
        JooqContext jooqContext = jooqContextFactory.createJooqContext(requestContext);
        ProductRecordDAO productRecordDAO = daoFactory.createProductRecordDAO(jooqContext);
        ProductLangRecordDAO productLangRecordDAO = daoFactory.createProductLangRecordDAO(jooqContext);

        productRecordDAO.updateDTO(product);

        List<ProductLangDTO> insertXLangs = new ArrayList<>();
        List<ProductLangDTO> updateXLangs = new ArrayList<>();
        List<ProductLangDTO> deleteXLangs = new ArrayList<>();
        for (ProductLangDTO xLang : product.getLangs()) {
            xLang.setProductId(product.getProductId());
            if (xLang.getDeleteFlag()) {
                deleteXLangs.add(xLang);
            } else if (xLang.getInsertFlag()) {
                insertXLangs.add(xLang);
            } else {
                updateXLangs.add(xLang);
            }
        }
        productLangRecordDAO.deleteDTOs(deleteXLangs);
        productLangRecordDAO.insertDTOs(insertXLangs);
        productLangRecordDAO.updateDTOs(updateXLangs);
        return product;
    }

    /**
     * note: we can use quarkus transactional,
     * because we use the same datasource for quarkus as well as for jooq,
     * so quarkus can be our transaction-manager.
     */
    @Transactional
    public void delete(final RequestContext requestContext, final ProductDTO product) {
        JooqContext jooqContext = jooqContextFactory.createJooqContext(requestContext);
        ProductRecordDAO productRecordDAO = daoFactory.createProductRecordDAO(jooqContext);
        ProductLangRecordDAO productLangRecordDAO = daoFactory.createProductLangRecordDAO(jooqContext);

        // we do use the explicit delete-by-id methods here, because they are the most performant.
        productLangRecordDAO.deleteByProductId(product.getProductId());
        productRecordDAO.deleteById(product.getProductId());
    }

    /**
     * Trying out streaming
     *
     * @param requestContext requestContext
     * @return stream
     */
    public Stream<ProductDTO> streamAll(final RequestContext requestContext) {
        JooqContext jooqContext = jooqContextFactory.createJooqContext(requestContext);
        ProductLangRecordDAO productLangRecordDAO = daoFactory.createProductLangRecordDAO(jooqContext);

        // TODO: try to find how this can be put into the Abstraction,
        Stream<ProductRecord> stream1 = jooqContext.getCtx()
                .selectFrom(Product.PRODUCT)
                .fetchSize(100)
                .fetchStream();
        Stream<List<ProductRecord>> chunkStream = chunk(stream1, 100);

        Stream<ProductDTO> resultStream = chunkStream.map(records -> {
            List<Long> ids = new ArrayList<>();
            for (ProductRecord record : records) {
                ids.add(record.getProductId());
            }
            List<ProductLangRecord> xLangs = productLangRecordDAO.fetchAllByProductsIds(ids);

            List<ProductDTO> products = new ArrayList<>();
            for (ProductRecord record : records) {
                ProductDTO product = record.into(new ProductDTO());

                List<ProductLangDTO> productLangs = new ArrayList<>();
                for (ProductLangRecord lang : xLangs) {
                    ProductLangDTO productLang = lang.into(new ProductLangDTO());
                    if (productLang.getProductId().equals(product.getProductId())) {
                        productLangs.add(productLang);
                        if (productLang.getLangId().equals(requestContext.getLangId())) {
                            product.setLang(productLang);
                        }
                    }
                }
                product.setLangs(productLangs);
                products.add(product);
            }
            return products;
        }).flatMap(List::stream);

        return resultStream;
    }

    /**
     * Helper function to chunk a stream of items into a stream of List of items.
     *
     * @param stream stream
     * @param size chunk-size of each chunk
     * @return stream of list of items.
     */
    Stream<List<ProductRecord>> chunk(Stream<ProductRecord> stream, int size) {
        Iterator<ProductRecord> iterator = stream.iterator();
        Iterator<List<ProductRecord>> listIterator = new Iterator<>() {

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public List<ProductRecord> next() {
                List<ProductRecord> result = new ArrayList<>(size);
                for (int i = 0; i < size && iterator.hasNext(); i++) {
                    result.add(iterator.next());
                }
                return result;
            }
        };
        return StreamSupport.stream(((Iterable<List<ProductRecord>>) () -> listIterator).spliterator(), false);
    }
}
