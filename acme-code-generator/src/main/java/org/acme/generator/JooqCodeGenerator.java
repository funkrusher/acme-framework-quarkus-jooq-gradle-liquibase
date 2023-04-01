package org.acme.generator;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.time.Duration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JooqCodeGenerator {

    public static void main(String[] args) throws Exception {
        // Start the MariaDB test container
        MariaDBContainer container = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.6.1"))
                .withDatabaseName("testshop")
                .withUsername("tester")
                .withPassword("test123");
        container.start();
        Awaitility.await()
                .atMost(Duration.ofSeconds(30))
                .pollInterval(Duration.ofSeconds(1))
                .until(container::isRunning);

        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Current working directory is: " + currentDirectory);

        try {
            // Configure Liquibase
            ResourceAccessor resourceAccessor = new DirectoryResourceAccessor(new File("../acme-backend/src/main/resources/liquibase"));
            Connection connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
            Liquibase liquibase = new liquibase.Liquibase("changelog.xml", resourceAccessor,
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection)));
            liquibase.update("");
            connection.close();

            // Generate JOOQ code programmatically
            Configuration configuration = new Configuration()
                    .withJdbc(new Jdbc()
                            .withDriver("org.mariadb.jdbc.Driver")
                            .withUrl(container.getJdbcUrl())
                            .withUser(container.getUsername())
                            .withPassword(container.getPassword()))
                    .withGenerator(new Generator()
                            .withName("org.acme.generator.MyGenerator")
                            .withGenerate(new Generate()
                                    .withInterfaces(true)
                                    .withSerializableInterfaces(true)
                                    .withDaos(true)
                                    .withValidationAnnotations(true)
                            )
                            .withStrategy(new Strategy()
                                    .withName("org.acme.generator.MyGeneratorStrategy")
                            )
                            .withDatabase(new Database()
                                    .withName("org.jooq.meta.mariadb.MariaDBDatabase")
                                    .withIncludes("testshop.*")
                                    .withExcludes("")
                                    .withForcedTypes(new ForcedType()
                                            .withName("BOOLEAN")
                                            .withIncludeTypes("(?i:TINYINT\\(1\\))"))
                            )
                            .withTarget(new Target()
                                    .withPackageName("org.acme.generated")
                                    .withDirectory("src/main/generated")));

            GenerationTool.generate(configuration);

        } finally {
            // Stop the MariaDB test container
            container.stop();
        }
    }
}