/*
 * This file is generated by jOOQ.
 */
package org.acme.generated.jooq_testshop.tables.daos;


import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.acme.generated.jooq_testshop.tables.Lang;
import org.acme.generated.jooq_testshop.tables.records.LangRecord;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Valid
public class LangDao extends DAOImpl<LangRecord, org.acme.generated.jooq_testshop.tables.dtos.Lang, Integer> {

    /**
     * Create a new LangDao without any configuration
     */
    public LangDao() {
        super(Lang.LANG, org.acme.generated.jooq_testshop.tables.dtos.Lang.class);
    }

    /**
     * Create a new LangDao with an attached configuration
     */
    public LangDao(Configuration configuration) {
        super(Lang.LANG, org.acme.generated.jooq_testshop.tables.dtos.Lang.class, configuration);
    }

    @Override
    public Integer getId(org.acme.generated.jooq_testshop.tables.dtos.Lang object) {
        return object.getLangId();
    }

    /**
     * Fetch records that have <code>langId BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.acme.generated.jooq_testshop.tables.dtos.Lang> fetchRangeOfLangid(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Lang.LANG.LANGID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>langId IN (values)</code>
     */
    public List<org.acme.generated.jooq_testshop.tables.dtos.Lang> fetchByLangid(Integer... values) {
        return fetch(Lang.LANG.LANGID, values);
    }

    /**
     * Fetch a unique record that has <code>langId = value</code>
     */
    public org.acme.generated.jooq_testshop.tables.dtos.Lang fetchOneByLangid(Integer value) {
        return fetchOne(Lang.LANG.LANGID, value);
    }

    /**
     * Fetch a unique record that has <code>langId = value</code>
     */
    public Optional<org.acme.generated.jooq_testshop.tables.dtos.Lang> fetchOptionalByLangid(Integer value) {
        return fetchOptional(Lang.LANG.LANGID, value);
    }

    /**
     * Fetch records that have <code>code BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.acme.generated.jooq_testshop.tables.dtos.Lang> fetchRangeOfCode(String lowerInclusive, String upperInclusive) {
        return fetchRange(Lang.LANG.CODE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>code IN (values)</code>
     */
    public List<org.acme.generated.jooq_testshop.tables.dtos.Lang> fetchByCode(String... values) {
        return fetch(Lang.LANG.CODE, values);
    }

    /**
     * Fetch records that have <code>description BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.acme.generated.jooq_testshop.tables.dtos.Lang> fetchRangeOfDescription(String lowerInclusive, String upperInclusive) {
        return fetchRange(Lang.LANG.DESCRIPTION, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>description IN (values)</code>
     */
    public List<org.acme.generated.jooq_testshop.tables.dtos.Lang> fetchByDescription(String... values) {
        return fetch(Lang.LANG.DESCRIPTION, values);
    }
}
