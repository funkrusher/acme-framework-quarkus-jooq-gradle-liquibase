package org.acme.daos.record;

import org.acme.daos.AbstractRecordDAO;
import org.acme.generated.testshop.tables.Lang;
import org.acme.generated.testshop.tables.interfaces.ILang;
import org.acme.generated.testshop.tables.records.LangRecord;
import org.acme.jooq.JooqContext;

/**
 * LangRecordDAO
 */
public class LangRecordDAO extends AbstractRecordDAO<LangRecord, ILang, Integer> {

    public LangRecordDAO(JooqContext jooqContext) {
        super(jooqContext, Lang.LANG);
    }

    @Override
    public Integer getId(LangRecord object) {
        return object.getLangId();
    }
}
