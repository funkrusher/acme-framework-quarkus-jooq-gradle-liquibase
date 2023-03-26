package org.acme.test;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.awaitility.Awaitility;
import org.flywaydb.core.Flyway;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

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
                .withDatabaseName("testshop")
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
        migrateDatabase(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        return jdbcUrl;
    }

    @Override
    public void stop() {
        container.stop();
    }

    private void createDatabase(String jdbcUrl, String username, String password) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS testshop");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void migrateDatabase(String jdbcUrl, String username, String password) {
        Flyway.configure()
                .dataSource(jdbcUrl, username, password)
                .locations("db/migration")
                .load()
                .migrate();
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