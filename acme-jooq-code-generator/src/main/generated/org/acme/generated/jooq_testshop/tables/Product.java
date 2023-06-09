/*
 * This file is generated by jOOQ.
 */
package org.acme.generated.jooq_testshop.tables;


import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.acme.generated.jooq_testshop.JooqTestshop;
import org.acme.generated.jooq_testshop.Keys;
import org.acme.generated.jooq_testshop.tables.records.ProductRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
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
public class Product extends TableImpl<ProductRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>jooq_testshop.product</code>
     */
    public static final Product PRODUCT = new Product();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProductRecord> getRecordType() {
        return ProductRecord.class;
    }

    /**
     * The column <code>jooq_testshop.product.productId</code>.
     */
    public final TableField<ProductRecord, Long> PRODUCTID = createField(DSL.name("productId"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>jooq_testshop.product.clientId</code>.
     */
    public final TableField<ProductRecord, Integer> CLIENTID = createField(DSL.name("clientId"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>jooq_testshop.product.price</code>.
     */
    public final TableField<ProductRecord, BigDecimal> PRICE = createField(DSL.name("price"), SQLDataType.DECIMAL(10, 2).nullable(false), this, "");

    /**
     * The column <code>jooq_testshop.product.createdAt</code>.
     */
    public final TableField<ProductRecord, LocalDateTime> CREATEDAT = createField(DSL.name("createdAt"), SQLDataType.LOCALDATETIME(0).nullable(false).defaultValue(DSL.field("current_timestamp()", SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>jooq_testshop.product.updatedAt</code>.
     */
    public final TableField<ProductRecord, LocalDateTime> UPDATEDAT = createField(DSL.name("updatedAt"), SQLDataType.LOCALDATETIME(0).nullable(false).defaultValue(DSL.field("current_timestamp()", SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>jooq_testshop.product.deleted</code>.
     */
    public final TableField<ProductRecord, Boolean> DELETED = createField(DSL.name("deleted"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field("0", SQLDataType.BOOLEAN)), this, "");

    private Product(Name alias, Table<ProductRecord> aliased) {
        this(alias, aliased, null);
    }

    private Product(Name alias, Table<ProductRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>jooq_testshop.product</code> table reference
     */
    public Product(String alias) {
        this(DSL.name(alias), PRODUCT);
    }

    /**
     * Create an aliased <code>jooq_testshop.product</code> table reference
     */
    public Product(Name alias) {
        this(alias, PRODUCT);
    }

    /**
     * Create a <code>jooq_testshop.product</code> table reference
     */
    public Product() {
        this(DSL.name("product"), null);
    }

    public <O extends Record> Product(Table<O> child, ForeignKey<O, ProductRecord> key) {
        super(child, key, PRODUCT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JooqTestshop.JOOQ_TESTSHOP;
    }

    @Override
    public Identity<ProductRecord, Long> getIdentity() {
        return (Identity<ProductRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<ProductRecord> getPrimaryKey() {
        return Keys.KEY_PRODUCT_PRIMARY;
    }

    @Override
    public List<ForeignKey<ProductRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FK_PRODUCT_CLIENTID);
    }

    private transient Client _fk_product_clientId;

    /**
     * Get the implicit join path to the <code>jooq_testshop.client</code>
     * table.
     */
    public Client fk_product_clientId() {
        if (_fk_product_clientId == null)
            _fk_product_clientId = new Client(this, Keys.FK_PRODUCT_CLIENTID);

        return _fk_product_clientId;
    }

    @Override
    public Product as(String alias) {
        return new Product(DSL.name(alias), this);
    }

    @Override
    public Product as(Name alias) {
        return new Product(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Product rename(String name) {
        return new Product(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Product rename(Name name) {
        return new Product(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Integer, BigDecimal, LocalDateTime, LocalDateTime, Boolean> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
