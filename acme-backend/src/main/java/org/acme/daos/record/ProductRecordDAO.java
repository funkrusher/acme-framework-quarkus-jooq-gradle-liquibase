package org.acme.daos.record;

import org.acme.daos.AbstractRecordDAO;
import org.acme.generated.testshop.tables.Product;
import org.acme.generated.testshop.tables.interfaces.IProduct;
import org.acme.generated.testshop.tables.records.ProductRecord;
import org.acme.jooq.JooqContext;

/**
 * ProductRecordDAO
 */
public class ProductRecordDAO extends AbstractRecordDAO<ProductRecord, IProduct, Long> {

    public ProductRecordDAO(JooqContext jooqContext) {
        super(jooqContext, Product.PRODUCT);
    }

    @Override
    public Long getId(ProductRecord object) {
        return object.getProductId();
    }
}
