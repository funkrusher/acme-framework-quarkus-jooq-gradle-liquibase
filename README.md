# Quarkus demo: jOOQ with Flyway, RESTEasy, Gradle, Testcontainers

This is a minimal CRUD service exposing a couple of endpoints over REST. 

Instead of using the typical Hibernate/ORM, we will explore jOOQ Object Oriented Querying and the possible benefits of using such a more dynamic approach to database operations in a multi-tenancy + multi-language context.

Under the hood, the code is using:
- Quarkus Framework
  - RESTEasy to expose the REST endpoints
  - JUnit5 Unit-Testing
  - Db-Transactions safely coordinated by the Narayana Transaction Manager
  - Swagger-UI Support
- jOOQ Framework
  - Object Oriented Querying on the database
  - Db-Schema-To-Code-Generator
- Flyway Db-Migrations
- Mariadb-Testcontainer for Unit-Tests and Code-Generator
- Gradle Build
  - Multi-Module project for shared-libraries approach 
- Customizable Helpers
    - own DAO-Abstraction that you can extend from and fine tune.
    - own Pojo-Abstraction with Modified-Fields detection support.
    - own RemotePagination Pojo to use for remote pagination

In the folder `./docs` you can find specific documentations about the different concepts this small seed-project implements, so you can benefit from them.

## Requirements

To compile and run this demo you will need:

- JDK 17+
- GraalVM
- Optional: Quarkus Plugin in Intellij-IDEA

In addition, you will need either a Mariadb database, or Docker to run one.
The project has been set up specifically with Intellij IDEA compatibility in mind.

### Configuring GraalVM and JDK 17+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 17+ `java` command is on the path. This is also important if you use Intellij IDEA.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

### Configuring application.properties

Copy the file `acme-backend/src/main/resources/application-template.properties` to `application.properties`.

Edit the file `acme-backend/src/main/resources/application.properties` in your editor of choice and set the following settings for a connection with your Mariadb database:
```code
quarkus.datasource.jdbc.url=jdbc:mariadb://localhost:3306/testshop
quarkus.datasource.username=xxx
quarkus.datasource.password=xxx
```

## Running the demo

Start the Demo from the Console with following command:
```code
./gradlew --console=plain quarkusDev
```
You can then navigate your webbrowser directly to the swagger-ui:
- http://localhost:8080/q/swagger-ui/

## Running the jOOQ Code-Generator

Start the jOOQ Code-Generator from the Console with following command:
```code
./gradlew generateJooqCode
```
The generated code will reside in the folder `acme-code-generator/src/main/generated`. The generator will fire up a mariadb-testcontainer automatically, apply the flyway-migrations to it and will then generate the code from this database-schema. Afterwards the testcontainer is stopped again. 

## Running the Unit-Tests

Start the Unit-Tests from the Console with following command:
```code
./gradlew test
```
The testing-framework will fire up a mariadb-testcontainer automatically and will apply the flyway-migrations to it. 
This way the Unit-Tests can expect a real database to be available behind the tested code, and with the help of jOOQ the expected database-content can be validated after each test.

## Dockerizing the application

Start the Native Build from the Console with following command:
```shell script
./gradlew clean buildNative "-Dquarkus.profile=dockerized"
```
It will use the `am-backend/src/main/resources/application-dockerized.properties` as configuration and will do a native build with it. 
The build produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory along with other files.

If you want to execute the app as a docker-container just start it as follows, after running the build.
```shell script
cd ./acme-backend
docker-compose up --build
```
This will start up a docker-container build with the `acme-backend/src/main/docker/Dockerfile.jvm` which will use the `build/quarkus-app` directory, we have created with our build and start up the `quarkus-run.jar`
After the docker-container has started we can open a rest-route in our webbrowser and it should work:
- http://localhost:8080/products/1

## Intellij IDEA

All the described operations can also be started up from within the Intellij IDEA.
1. Make sure that the project does not contain the subfolders: `.idea` and `.gradle`, and also delete all `build` folders within the projects subdirectories. 
2. Now open the project via `File`->`Open`.
3. The project should now be build automatically.

## Third-Party Versions Balancing

The used versions of third-party libraries must be balanced with each other. 

we need to use jOOQ 3.16 instead of the latest jOOQ, because testcontainers only provides a mariadb 10.6 in its latest version,
and mariadb 10.6 is only supported by jOOQ 3.16. See:
- https://www.jooq.org/download/support-matrix

We need to use Quarkus 3.0.0.Beta, because the jOOQ Code Generator already creates the "jakarta.*" package imports for stuff that was in "javax.*" before.
Quarkus only starts to support this migration of Jakarta, beginning with 3.0.0.

We also can check conflicting dependencies, with gradlew. For example. The following command would check the dependency `validation-api` in our module `acme-backend` and show as all versions of this (possibly transitive) dependency in the runtime classpath: 
```code
gradlew -p acme-backend dependencyInsight --dependency validation-api --configuration runtimeClasspath
```

## Related Guides
- GraalVM ([installation-guide](https://www.graalvm.org/latest/docs/getting-started/windows/), [release-download](https://github.com/graalvm/graalvm-ce-builds/releases)): GraalVM Installation
- Flyway ([guide](https://quarkus.io/guides/flyway)): Handle your database schema migrations
- Gradle+Quarkus ([guide](https://quarkus.io/guides/gradle-tooling)): Building quarkus apps with gradle
- Gradle+IDEA ([gradle-guide](https://docs.gradle.org/current/userguide/idea_plugin.html), [idea-guide](https://www.jetbrains.com/help/idea/work-with-gradle-projects.html#project_encodings)): Setting up gradle with IDEA
- jOOQ ([guide](https://www.jooq.org/doc/3.18/manual/)): Handle your database querying
    - Insert/Update Only Changed-Values ([read](https://blog.jooq.org/orms-should-update-changed-values-not-just-modified-ones/)): Read about the topic why ORMs should update "changed" values, not just "modified" ones.

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.