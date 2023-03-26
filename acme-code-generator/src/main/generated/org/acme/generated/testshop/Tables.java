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


/**
 * Convenience access to all tables in testshop.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Valid
public class Tables {

    /**
     * The table <code>testshop.client</code>.
     */
    public static final Client CLIENT = Client.CLIENT;

    /**
     * The table <code>testshop.flyway_schema_history</code>.
     */
    public static final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>testshop.lang</code>.
     */
    public static final Lang LANG = Lang.LANG;

    /**
     * The table <code>testshop.product</code>.
     */
    public static final Product PRODUCT = Product.PRODUCT;

    /**
     * The table <code>testshop.product_lang</code>.
     */
    public static final ProductLang PRODUCT_LANG = ProductLang.PRODUCT_LANG;
}
