package org.acme.daos.view;

import org.acme.daos.AbstractViewDAO;
import org.acme.dtos.ProductDTO;
import org.acme.dtos.ProductLangDTO;
import org.acme.generated.jooq_testshop.tables.Product;
import org.acme.generated.jooq_testshop.tables.ProductLang;
import org.acme.generated.jooq_testshop.tables.records.ProductLangRecord;
import org.acme.generated.jooq_testshop.tables.records.ProductRecord;
import org.acme.jooq.JooqContext;
import org.jooq.Record;
import org.jooq.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProductViewDAO
 */
public class ProductViewDAO extends AbstractViewDAO<ProductRecord, ProductDTO, Long> {

    public ProductViewDAO(JooqContext jooqContext) {
        super(jooqContext, Product.PRODUCT);
    }

    @Override
    protected Long getId(ProductRecord object) {
        return object.getProductId();
    }

    @Override
    protected List<Field<?>> getViewFields() {
        List<Field<?>> fields = new ArrayList<>();
        fields.addAll(List.of(table().fields()));
        fields.addAll(List.of(ProductLang.PRODUCT_LANG.fields()));
        return fields;
    }

    @Override
    protected TableOnConditionStep<Record> getViewJoins() {
        return Product.PRODUCT
                .leftJoin(ProductLang.PRODUCT_LANG)
                .on(ProductLang.PRODUCT_LANG.PRODUCTID
                        .eq(Product.PRODUCT.PRODUCTID));
    }

    @Override
    protected List<ProductDTO> recordsToView(List<Record> records) {
        List<ProductDTO> products = new ArrayList<>();
        Map<Long, ProductDTO> productMap = new HashMap<>();

        for (Record record : records) {
            Long productId = record.get(Product.PRODUCT.PRODUCTID);
            ProductDTO productDTO = productMap.get(productId);

            if (productDTO == null) {
                ProductRecord productRecord = record.into(new ProductRecord());
                productDTO = productRecord.into(new ProductDTO());
                List<ProductLangDTO> xLangs = new ArrayList<>();
                productDTO.setLangs(xLangs);
                products.add(productDTO);
                productMap.put(productId, productDTO);
            }
            ProductLangRecord xLangRecord = record.into(new ProductLangRecord());
            ProductLangDTO xLang = xLangRecord.into(new ProductLangDTO());
            productDTO.getLangs().add(xLang);

            if (xLang.getLangId().equals(requestContext().getLangId())) {
                productDTO.setLang(xLang);
            }
        }
        return products;
    }
}
