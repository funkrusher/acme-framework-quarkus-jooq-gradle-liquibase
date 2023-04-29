package org.acme.daos.record;

import org.acme.daos.AbstractRecordDAO;
import org.acme.generated.jooq_testshop.tables.Client;
import org.acme.generated.jooq_testshop.tables.User;
import org.acme.generated.jooq_testshop.tables.interfaces.IClient;
import org.acme.generated.jooq_testshop.tables.interfaces.IUser;
import org.acme.generated.jooq_testshop.tables.records.ClientRecord;
import org.acme.generated.jooq_testshop.tables.records.UserRecord;
import org.acme.jooq.JooqContext;

/**
 * UserRecordDAO
 */
public class UserRecordDAO extends AbstractRecordDAO<UserRecord, IUser, Integer> {

    public UserRecordDAO(JooqContext jooqContext) {
        super(jooqContext, User.USER);
    }

    @Override
    public Integer getId(UserRecord object) {
        return object.getUserId();
    }
}
