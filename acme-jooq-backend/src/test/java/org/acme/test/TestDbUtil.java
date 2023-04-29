package org.acme.test;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.testcontainers.containers.MariaDBContainer;

/**
 * TestDbUtil
 * <p>
 * Helper-class for access and interaction with the testcontainers-mariadb database from within the unit-tests.
 * </p>
 */
public class TestDbUtil {

    private MariaDBContainer<?> container;

    public TestDbUtil(MariaDBContainer<?> container) {
        this.container = container;
    }

    public DSLContext createDSLContext() {
        return DSL.using(this.container.getJdbcUrl(), this.container.getUsername(), this.container.getPassword());
    }
}
