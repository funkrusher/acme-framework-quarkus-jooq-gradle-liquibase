package org.acme.generator;

import org.flywaydb.core.Flyway;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;


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

        try {
            // Configure Flyway
            Flyway flyway = Flyway.configure()
                    .dataSource(container.getJdbcUrl(), container.getUsername(), container.getPassword())
                    .locations("filesystem:../acme-backend/src/main/resources/db/migration")
                    .load();

            // Migrate the database using Flyway
            flyway.migrate();

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