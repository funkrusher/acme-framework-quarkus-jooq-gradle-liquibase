/*
 * This file is generated by jOOQ.
 */
package org.acme.generated.testshop.tables;


import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.List;

import org.acme.generated.testshop.Keys;
import org.acme.generated.testshop.Testshop;
import org.acme.generated.testshop.tables.records.ProductLangRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Valid
public class ProductLang extends TableImpl<ProductLangRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>testshop.product_lang</code>
     */
    public static final ProductLang PRODUCT_LANG = new ProductLang();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProductLangRecord> getRecordType() {
        return ProductLangRecord.class;
    }

    /**
     * The column <code>testshop.product_lang.productId</code>.
     */
    public final TableField<ProductLangRecord, Long> PRODUCTID = createField(DSL.name("productId"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>testshop.product_lang.langId</code>.
     */
    public final TableField<ProductLangRecord, Integer> LANGID = createField(DSL.name("langId"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>testshop.product_lang.name</code>.
     */
    public final TableField<ProductLangRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>testshop.product_lang.description</code>.
     */
    public final TableField<ProductLangRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.CLOB.nullable(false), this, "");

    private ProductLang(Name alias, Table<ProductLangRecord> aliased) {
        this(alias, aliased, null);
    }

    private ProductLang(Name alias, Table<ProductLangRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>testshop.product_lang</code> table reference
     */
    public ProductLang(String alias) {
        this(DSL.name(alias), PRODUCT_LANG);
    }

    /**
     * Create an aliased <code>testshop.product_lang</code> table reference
     */
    public ProductLang(Name alias) {
        this(alias, PRODUCT_LANG);
    }

    /**
     * Create a <code>testshop.product_lang</code> table reference
     */
    public ProductLang() {
        this(DSL.name("product_lang"), null);
    }

    public <O extends Record> ProductLang(Table<O> child, ForeignKey<O, ProductLangRecord> key) {
        super(child, key, PRODUCT_LANG);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Testshop.TESTSHOP;
    }

    @Override
    public UniqueKey<ProductLangRecord> getPrimaryKey() {
        return Keys.KEY_PRODUCT_LANG_PRIMARY;
    }

    @Override
    public List<ForeignKey<ProductLangRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FK_PRODUCT_LANG_PRODUCTID, Keys.FK_PRODUCT_LANG_LANGID);
    }

    private transient Product _fk_product_lang_productId;
    private transient Lang _fk_product_lang_langId;

    /**
     * Get the implicit join path to the <code>testshop.product</code> table.
     */
    public Product fk_product_lang_productId() {
        if (_fk_product_lang_productId == null)
            _fk_product_lang_productId = new Product(this, Keys.FK_PRODUCT_LANG_PRODUCTID);

        return _fk_product_lang_productId;
    }

    /**
     * Get the implicit join path to the <code>testshop.lang</code> table.
     */
    public Lang fk_product_lang_langId() {
        if (_fk_product_lang_langId == null)
            _fk_product_lang_langId = new Lang(this, Keys.FK_PRODUCT_LANG_LANGID);

        return _fk_product_lang_langId;
    }

    @Override
    public ProductLang as(String alias) {
        return new ProductLang(DSL.name(alias), this);
    }

    @Override
    public ProductLang as(Name alias) {
        return new ProductLang(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ProductLang rename(String name) {
        return new ProductLang(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ProductLang rename(Name name) {
        return new ProductLang(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, Integer, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
