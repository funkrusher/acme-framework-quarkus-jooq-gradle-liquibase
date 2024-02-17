package org.acme.daos.view;

import org.acme.daos.AbstractViewDAO;
import org.acme.dtos.ProductDTO;
import org.acme.dtos.ProductLangDTO;
import org.acme.generated.AbstractDTO;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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



    private List<Record> extractRecords(List<Record> records, List<Field<?>> uniqueFields) {
        Map<String, Record> uniqueRecords = new HashMap<>();
        for (Record record : records) {
            String key = "";
            for (Field<?> uniqueField : uniqueFields) {
                String test = record.getValue(uniqueField).toString();
                key += test;
            }
            uniqueRecords.put(key, record);
        }
        return uniqueRecords.values().stream().toList();
    }



    @Override
    protected List<ProductDTO> recordsToView(List<Record> records) {
        // uniquely get entries and swallow duplicates.
        List<ProductDTO> products = extractRecords(records, List.of(Product.PRODUCT.PRODUCTID))
                .stream().map(r -> r.into(new ProductRecord()).into(new ProductDTO())).toList();
         Map<Long, List<ProductLangDTO>> langs = extractRecords(records, List.of(ProductLang.PRODUCT_LANG.PRODUCTID, ProductLang.PRODUCT_LANG.LANGID))
                .stream().map(r -> r.into(new ProductLangRecord()).into(new ProductLangDTO()))
                .collect(groupingBy(ProductLangDTO::getProductId));

        for (ProductDTO product : products) {
            List<ProductLangDTO> productLangs = langs.get(product.getProductId());
            product.setLangs(productLangs);
            for (ProductLangDTO productLang : productLangs) {
                if (productLang.getLangId().equals(requestContext().getLangId())) {
                    product.setLang(productLang);
                }
            }
        }
        return products;
    }
}
