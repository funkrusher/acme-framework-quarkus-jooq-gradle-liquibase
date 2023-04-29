package org.acme.test;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.awaitility.Awaitility;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * TestDbLifecycleManager
 * <p>
 * - starts mariadb-testcontainer
 * - apply flyway migration
 * - provides mariadb-testcontainer configuration to datasource-configuration of application in application.properties.
 * </p>
 */
public class TestDbLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private MariaDBContainer<?> container;

    @Override
    public Map<String, String> start() {
        container = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.6.1"))
                .withDatabaseName("jooq_testshop")
                .withUsername("tester")
                .withPassword("test123");

        container.start();
        waitUntilContainerStarted();

        Map<String, String> jdbcUrl = new HashMap<>();
        jdbcUrl.put("quarkus.datasource.url", container.getJdbcUrl());
        jdbcUrl.put("mariadb.testcontainer.host", "localhost");
        jdbcUrl.put("mariadb.testcontainer.port", String.valueOf(container.getFirstMappedPort()));
        jdbcUrl.put("mariadb.testcontainer.username", String.valueOf(container.getUsername()));
        jdbcUrl.put("mariadb.testcontainer.password", String.valueOf(container.getPassword()));
        jdbcUrl.put("mariadb.testcontainer.database", String.valueOf(container.getDatabaseName()));

        createDatabase(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        try {
            migrateDatabase(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jdbcUrl;
    }

    @Override
    public void stop() {
        container.stop();
    }

    private void createDatabase(String jdbcUrl, String username, String password) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS jooq_testshop");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void migrateDatabase(String jdbcUrl, String username, String password) throws FileNotFoundException, SQLException, LiquibaseException {
        // Configure Liquibase
        ResourceAccessor resourceAccessor = new DirectoryResourceAccessor(new File("src/main/resources/liquibase"));
        Connection connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        Liquibase liquibase = new liquibase.Liquibase("changelog.xml", resourceAccessor,
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection)));
        liquibase.update("");
        connection.close();
    }

    private void waitUntilContainerStarted() {
        Awaitility.await()
                .atMost(Duration.ofSeconds(30))
                .pollInterval(Duration.ofSeconds(1))
                .until(container::isRunning);
    }

    @Override
    public void inject(Object testInstance) {

    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(new TestDbUtil(container), new TestInjector.AnnotatedAndMatchesType(InjectTestDbUtil.class, TestDbUtil.class));
    }
}