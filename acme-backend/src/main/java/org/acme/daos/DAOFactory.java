package org.acme.daos;

import org.acme.daos.record.ClientRecordDAO;
import org.acme.daos.record.LangRecordDAO;
import org.acme.daos.record.ProductLangRecordDAO;
import org.acme.daos.record.ProductRecordDAO;
import org.acme.daos.view.ProductViewDAO;
import org.acme.jooq.JooqContext;

import jakarta.enterprise.context.ApplicationScoped;
import org.jooq.Record;

/**
 * DAOFactory to create instances of context-scoped daos.
 */
@ApplicationScoped
public class DAOFactory {

    public ClientRecordDAO createClientRecordDAO(JooqContext jooqContext) {
        return new ClientRecordDAO(jooqContext);
    }

    public ProductRecordDAO createProductRecordDAO(JooqContext jooqContext) {
        return new ProductRecordDAO(jooqContext);
    }

    public ProductViewDAO createProductViewDAO(JooqContext jooqContext) {
        return new ProductViewDAO(jooqContext);
    }

    public ProductLangRecordDAO createProductLangRecordDAO(JooqContext jooqContext) {
        return new ProductLangRecordDAO(jooqContext);
    }

    public LangRecordDAO createLangRecordDAO(JooqContext jooqContext) {
        return new LangRecordDAO(jooqContext);
    }
}
