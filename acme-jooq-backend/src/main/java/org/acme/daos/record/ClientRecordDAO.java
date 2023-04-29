package org.acme.daos.record;

import org.acme.daos.AbstractRecordDAO;
import org.acme.generated.jooq_testshop.tables.Client;
import org.acme.generated.jooq_testshop.tables.interfaces.IClient;
import org.acme.generated.jooq_testshop.tables.records.ClientRecord;
import org.acme.jooq.JooqContext;

/**
 * ClientRecordDAO
 */
public class ClientRecordDAO extends AbstractRecordDAO<ClientRecord, IClient, Integer> {

    public ClientRecordDAO(JooqContext jooqContext) {
        super(jooqContext, Client.CLIENT);
    }

    @Override
    public Integer getId(ClientRecord object) {
        return object.getClientId();
    }
}
