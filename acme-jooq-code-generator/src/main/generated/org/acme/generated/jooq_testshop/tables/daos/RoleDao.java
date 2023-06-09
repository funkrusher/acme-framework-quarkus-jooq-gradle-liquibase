/*
 * This file is generated by jOOQ.
 */
package org.acme.generated.jooq_testshop.tables.daos;


import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.acme.generated.jooq_testshop.tables.Role;
import org.acme.generated.jooq_testshop.tables.records.RoleRecord;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Valid
public class RoleDao extends DAOImpl<RoleRecord, org.acme.generated.jooq_testshop.tables.dtos.Role, String> {

    /**
     * Create a new RoleDao without any configuration
     */
    public RoleDao() {
        super(Role.ROLE, org.acme.generated.jooq_testshop.tables.dtos.Role.class);
    }

    /**
     * Create a new RoleDao with an attached configuration
     */
    public RoleDao(Configuration configuration) {
        super(Role.ROLE, org.acme.generated.jooq_testshop.tables.dtos.Role.class, configuration);
    }

    @Override
    public String getId(org.acme.generated.jooq_testshop.tables.dtos.Role object) {
        return object.getRoleId();
    }

    /**
     * Fetch records that have <code>roleId BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.acme.generated.jooq_testshop.tables.dtos.Role> fetchRangeOfRoleid(String lowerInclusive, String upperInclusive) {
        return fetchRange(Role.ROLE.ROLEID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>roleId IN (values)</code>
     */
    public List<org.acme.generated.jooq_testshop.tables.dtos.Role> fetchByRoleid(String... values) {
        return fetch(Role.ROLE.ROLEID, values);
    }

    /**
     * Fetch a unique record that has <code>roleId = value</code>
     */
    public org.acme.generated.jooq_testshop.tables.dtos.Role fetchOneByRoleid(String value) {
        return fetchOne(Role.ROLE.ROLEID, value);
    }

    /**
     * Fetch a unique record that has <code>roleId = value</code>
     */
    public Optional<org.acme.generated.jooq_testshop.tables.dtos.Role> fetchOptionalByRoleid(String value) {
        return fetchOptional(Role.ROLE.ROLEID, value);
    }
}
