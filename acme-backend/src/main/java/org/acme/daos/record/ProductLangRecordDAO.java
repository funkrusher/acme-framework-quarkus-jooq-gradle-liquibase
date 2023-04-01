package org.acme.daos.record;

import org.acme.daos.AbstractRecordDAO;
import org.acme.generated.testshop.tables.ProductLang;
import org.acme.generated.testshop.tables.interfaces.IProductLang;
import org.acme.generated.testshop.tables.records.ProductLangRecord;
import org.acme.jooq.JooqContext;
import org.jooq.Record2;

import java.util.List;

/**
 * ProductLangRecordDAO
 */
public class ProductLangRecordDAO extends AbstractRecordDAO<ProductLangRecord, IProductLang, Record2<Long, Integer>> {

    public ProductLangRecordDAO(JooqContext jooqContext) {
        super(jooqContext, ProductLang.PRODUCT_LANG);
    }

    @Override
    public Record2<Long, Integer> getId(ProductLangRecord object) {
        return compositeKeyRecord(object.getProductId(), object.getLangId());
    }

    /**
     * Delete all by productId
     *
     * @param productId
     * @return delete count
     */
    public int deleteByProductId(Long productId) {
        return ctx().deleteFrom(table()).where(ProductLang.PRODUCT_LANG.PRODUCTID.eq(productId)).execute();
    }

    public List<ProductLangRecord> fetchAllByProductsIds(List<Long> productIds) {
        return ctx().selectFrom(table()).where(ProductLang.PRODUCT_LANG.PRODUCTID.in(productIds)).fetch();
    }

}
