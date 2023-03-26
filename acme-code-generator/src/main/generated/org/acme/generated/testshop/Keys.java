/*
 * This file is generated by jOOQ.
 */
package org.acme.generated.testshop;


import jakarta.validation.Valid;

import org.acme.generated.testshop.tables.Client;
import org.acme.generated.testshop.tables.FlywaySchemaHistory;
import org.acme.generated.testshop.tables.Lang;
import org.acme.generated.testshop.tables.Product;
import org.acme.generated.testshop.tables.ProductLang;
import org.acme.generated.testshop.tables.records.ClientRecord;
import org.acme.generated.testshop.tables.records.FlywaySchemaHistoryRecord;
import org.acme.generated.testshop.tables.records.LangRecord;
import org.acme.generated.testshop.tables.records.ProductLangRecord;
import org.acme.generated.testshop.tables.records.ProductRecord;
import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * testshop.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Valid
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ClientRecord> KEY_CLIENT_PRIMARY = Internal.createUniqueKey(Client.CLIENT, DSL.name("KEY_client_PRIMARY"), new TableField[] { Client.CLIENT.CLIENTID }, true);
    public static final UniqueKey<FlywaySchemaHistoryRecord> KEY_FLYWAY_SCHEMA_HISTORY_PRIMARY = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, DSL.name("KEY_flyway_schema_history_PRIMARY"), new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
    public static final UniqueKey<LangRecord> KEY_LANG_PRIMARY = Internal.createUniqueKey(Lang.LANG, DSL.name("KEY_lang_PRIMARY"), new TableField[] { Lang.LANG.LANGID }, true);
    public static final UniqueKey<ProductRecord> KEY_PRODUCT_PRIMARY = Internal.createUniqueKey(Product.PRODUCT, DSL.name("KEY_product_PRIMARY"), new TableField[] { Product.PRODUCT.PRODUCTID }, true);
    public static final UniqueKey<ProductLangRecord> KEY_PRODUCT_LANG_PRIMARY = Internal.createUniqueKey(ProductLang.PRODUCT_LANG, DSL.name("KEY_product_lang_PRIMARY"), new TableField[] { ProductLang.PRODUCT_LANG.PRODUCTID, ProductLang.PRODUCT_LANG.LANGID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ProductRecord, ClientRecord> FK_PRODUCT_CLIENTID = Internal.createForeignKey(Product.PRODUCT, DSL.name("fk_product_clientId"), new TableField[] { Product.PRODUCT.CLIENTID }, Keys.KEY_CLIENT_PRIMARY, new TableField[] { Client.CLIENT.CLIENTID }, true);
    public static final ForeignKey<ProductLangRecord, LangRecord> FK_PRODUCT_LANG_LANGID = Internal.createForeignKey(ProductLang.PRODUCT_LANG, DSL.name("fk_product_lang_langId"), new TableField[] { ProductLang.PRODUCT_LANG.LANGID }, Keys.KEY_LANG_PRIMARY, new TableField[] { Lang.LANG.LANGID }, true);
    public static final ForeignKey<ProductLangRecord, ProductRecord> FK_PRODUCT_LANG_PRODUCTID = Internal.createForeignKey(ProductLang.PRODUCT_LANG, DSL.name("fk_product_lang_productId"), new TableField[] { ProductLang.PRODUCT_LANG.PRODUCTID }, Keys.KEY_PRODUCT_PRIMARY, new TableField[] { Product.PRODUCT.PRODUCTID }, true);
}
