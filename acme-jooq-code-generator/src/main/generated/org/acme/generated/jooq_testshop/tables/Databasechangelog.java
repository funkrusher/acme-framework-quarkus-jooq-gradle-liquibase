/*
 * This file is generated by jOOQ.
 */
package org.acme.generated.jooq_testshop.tables;


import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.acme.generated.jooq_testshop.JooqTestshop;
import org.acme.generated.jooq_testshop.tables.records.DatabasechangelogRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row14;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Valid
public class Databasechangelog extends TableImpl<DatabasechangelogRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>jooq_testshop.DATABASECHANGELOG</code>
     */
    public static final Databasechangelog DATABASECHANGELOG = new Databasechangelog();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DatabasechangelogRecord> getRecordType() {
        return DatabasechangelogRecord.class;
    }

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.ID</code>.
     */
    public final TableField<DatabasechangelogRecord, String> ID = createField(DSL.name("ID"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.AUTHOR</code>.
     */
    public final TableField<DatabasechangelogRecord, String> AUTHOR = createField(DSL.name("AUTHOR"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.FILENAME</code>.
     */
    public final TableField<DatabasechangelogRecord, String> FILENAME = createField(DSL.name("FILENAME"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.DATEEXECUTED</code>.
     */
    public final TableField<DatabasechangelogRecord, LocalDateTime> DATEEXECUTED = createField(DSL.name("DATEEXECUTED"), SQLDataType.LOCALDATETIME(0).nullable(false), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.ORDEREXECUTED</code>.
     */
    public final TableField<DatabasechangelogRecord, Integer> ORDEREXECUTED = createField(DSL.name("ORDEREXECUTED"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.EXECTYPE</code>.
     */
    public final TableField<DatabasechangelogRecord, String> EXECTYPE = createField(DSL.name("EXECTYPE"), SQLDataType.VARCHAR(10).nullable(false), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.MD5SUM</code>.
     */
    public final TableField<DatabasechangelogRecord, String> MD5SUM = createField(DSL.name("MD5SUM"), SQLDataType.VARCHAR(35).defaultValue(DSL.field("NULL", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.DESCRIPTION</code>.
     */
    public final TableField<DatabasechangelogRecord, String> DESCRIPTION = createField(DSL.name("DESCRIPTION"), SQLDataType.VARCHAR(255).defaultValue(DSL.field("NULL", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.COMMENTS</code>.
     */
    public final TableField<DatabasechangelogRecord, String> COMMENTS = createField(DSL.name("COMMENTS"), SQLDataType.VARCHAR(255).defaultValue(DSL.field("NULL", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.TAG</code>.
     */
    public final TableField<DatabasechangelogRecord, String> TAG = createField(DSL.name("TAG"), SQLDataType.VARCHAR(255).defaultValue(DSL.field("NULL", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.LIQUIBASE</code>.
     */
    public final TableField<DatabasechangelogRecord, String> LIQUIBASE = createField(DSL.name("LIQUIBASE"), SQLDataType.VARCHAR(20).defaultValue(DSL.field("NULL", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.CONTEXTS</code>.
     */
    public final TableField<DatabasechangelogRecord, String> CONTEXTS = createField(DSL.name("CONTEXTS"), SQLDataType.VARCHAR(255).defaultValue(DSL.field("NULL", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.LABELS</code>.
     */
    public final TableField<DatabasechangelogRecord, String> LABELS = createField(DSL.name("LABELS"), SQLDataType.VARCHAR(255).defaultValue(DSL.field("NULL", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>jooq_testshop.DATABASECHANGELOG.DEPLOYMENT_ID</code>.
     */
    public final TableField<DatabasechangelogRecord, String> DEPLOYMENT_ID = createField(DSL.name("DEPLOYMENT_ID"), SQLDataType.VARCHAR(10).defaultValue(DSL.field("NULL", SQLDataType.VARCHAR)), this, "");

    private Databasechangelog(Name alias, Table<DatabasechangelogRecord> aliased) {
        this(alias, aliased, null);
    }

    private Databasechangelog(Name alias, Table<DatabasechangelogRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>jooq_testshop.DATABASECHANGELOG</code> table
     * reference
     */
    public Databasechangelog(String alias) {
        this(DSL.name(alias), DATABASECHANGELOG);
    }

    /**
     * Create an aliased <code>jooq_testshop.DATABASECHANGELOG</code> table
     * reference
     */
    public Databasechangelog(Name alias) {
        this(alias, DATABASECHANGELOG);
    }

    /**
     * Create a <code>jooq_testshop.DATABASECHANGELOG</code> table reference
     */
    public Databasechangelog() {
        this(DSL.name("DATABASECHANGELOG"), null);
    }

    public <O extends Record> Databasechangelog(Table<O> child, ForeignKey<O, DatabasechangelogRecord> key) {
        super(child, key, DATABASECHANGELOG);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JooqTestshop.JOOQ_TESTSHOP;
    }

    @Override
    public Databasechangelog as(String alias) {
        return new Databasechangelog(DSL.name(alias), this);
    }

    @Override
    public Databasechangelog as(Name alias) {
        return new Databasechangelog(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Databasechangelog rename(String name) {
        return new Databasechangelog(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Databasechangelog rename(Name name) {
        return new Databasechangelog(name, null);
    }

    // -------------------------------------------------------------------------
    // Row14 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row14<String, String, String, LocalDateTime, Integer, String, String, String, String, String, String, String, String, String> fieldsRow() {
        return (Row14) super.fieldsRow();
    }
}
