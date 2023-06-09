/*
 * This file is generated by jOOQ.
 */
package org.acme.generated.jooq_testshop.tables;


import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.List;

import org.acme.generated.jooq_testshop.JooqTestshop;
import org.acme.generated.jooq_testshop.Keys;
import org.acme.generated.jooq_testshop.tables.records.UserRoleRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
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
public class UserRole extends TableImpl<UserRoleRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>jooq_testshop.user_role</code>
     */
    public static final UserRole USER_ROLE = new UserRole();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UserRoleRecord> getRecordType() {
        return UserRoleRecord.class;
    }

    /**
     * The column <code>jooq_testshop.user_role.userId</code>.
     */
    public final TableField<UserRoleRecord, Integer> USERID = createField(DSL.name("userId"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>jooq_testshop.user_role.roleId</code>.
     */
    public final TableField<UserRoleRecord, String> ROLEID = createField(DSL.name("roleId"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    private UserRole(Name alias, Table<UserRoleRecord> aliased) {
        this(alias, aliased, null);
    }

    private UserRole(Name alias, Table<UserRoleRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>jooq_testshop.user_role</code> table reference
     */
    public UserRole(String alias) {
        this(DSL.name(alias), USER_ROLE);
    }

    /**
     * Create an aliased <code>jooq_testshop.user_role</code> table reference
     */
    public UserRole(Name alias) {
        this(alias, USER_ROLE);
    }

    /**
     * Create a <code>jooq_testshop.user_role</code> table reference
     */
    public UserRole() {
        this(DSL.name("user_role"), null);
    }

    public <O extends Record> UserRole(Table<O> child, ForeignKey<O, UserRoleRecord> key) {
        super(child, key, USER_ROLE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JooqTestshop.JOOQ_TESTSHOP;
    }

    @Override
    public UniqueKey<UserRoleRecord> getPrimaryKey() {
        return Keys.KEY_USER_ROLE_PRIMARY;
    }

    @Override
    public List<ForeignKey<UserRoleRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FK_USER_ROLE_USERID, Keys.FK_USER_ROLE_ROLEID);
    }

    private transient User _fk_user_role_userId;
    private transient Role _fk_user_role_roleId;

    /**
     * Get the implicit join path to the <code>jooq_testshop.user</code> table.
     */
    public User fk_user_role_userId() {
        if (_fk_user_role_userId == null)
            _fk_user_role_userId = new User(this, Keys.FK_USER_ROLE_USERID);

        return _fk_user_role_userId;
    }

    /**
     * Get the implicit join path to the <code>jooq_testshop.role</code> table.
     */
    public Role fk_user_role_roleId() {
        if (_fk_user_role_roleId == null)
            _fk_user_role_roleId = new Role(this, Keys.FK_USER_ROLE_ROLEID);

        return _fk_user_role_roleId;
    }

    @Override
    public UserRole as(String alias) {
        return new UserRole(DSL.name(alias), this);
    }

    @Override
    public UserRole as(Name alias) {
        return new UserRole(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public UserRole rename(String name) {
        return new UserRole(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserRole rename(Name name) {
        return new UserRole(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
