# TAM CRM Service

## Description

This is a CRM service made in Java, provides the following features:

- Customer management (CRUD and profile image upload).
- User management.

## Technical Information

The project is based on a Hexagonal Architecture (using Ports and Adapters pattern) & DDD (Domain Driven Design), which
will decouple the business core from the technical details, externalizing and making them depend on the business needs,
leaving an easy testable and maintainable application.

API First approach has been followed, using OpenAPI to define the REST API.

Persistence is achieved using a PostgreSQL database for user and customer data, and S3 for customer profile images.

### Structure

The project is divided into the following modules:

- `domain`: Business entities and domain use case definitions (DDD).
- `application`: Domain use case specific implementations and port definitions.
- `infrastructure`: Driven port implementations (adapters) to interact with external systems (customer & user database,
  S3 for file uploading & cache for image URLs).
- `controller`: API controllers (Driver ports) and authentication implementation.
- `boot`: Application entry point.

### Code format

This project follows [Google Java Style](https://google.github.io/styleguide/javaguide.html) conventions.

## Development

### Requirements

- JDK 21 & Maven.
- Lombok plugin (installation depends on IDE).
- Docker (Integration Tests & Deployment) or Localstack/PostgreSQL to provide needed infrastructure.
- [google-java-format](https://github.com/google/google-java-format) plugin.

### Execution

To start setting things up execute the following, this will generate needed mappers and APIs (model & controller),
compile the project and execute unit tests:

```
mvn clean install
```

To execute the application (locally), make sure you have the needed infra running (read Deployment with Docker) and you
are using `local` Spring profile (`SPRING_PROFILES_ACTIVE`):

```
mvn spring-boot:run
```

### CI/CD

A basic CI/CD pipeline is provided using Github Actions, which will execute `verify` goal to generate JaCoCo coverage of
all unit and integration tests and check if the coverage is above the defined threshold.

If everything is OK, it will build and publish the application image to the Docker Hub registry.

## Testing

### Unit Tests

To execute unit tests use the following:

```
mvn clean test
```

### Integration Tests

To execute integration tests use the `integration-tests` profile:

```
mvn ... -P integration-tests
```

### Code Coverage

```
mvn clean verify
```

Executing `verify` goal will generate JaCoCo coverage reports, located
in `report-aggregate/target/site/jacoco-aggregate`

## Deployment

### Docker

To build the application image a `Dockerfile` is also provided, which is used by the `docker-compose` file.

The `docker-compose` file is provided to deploy the application and its dependencies (PostgreSQL, Liquibase, S3 with
Localstack & AWS CLI to initialize S3 bucket), leaving the usage as simple as:

```
docker-compose up
```

This command will make use of Liquibase to initialize the database schema, but it will also execute a second command in
order to create an `admin` user with password `password`, this is meant to be used only in a local environment.

`crm-service` container can be built and run separately, but it will need the other services to be up and running.

### Docker Hub Image

The application image is built and published to the Docker Hub registry using Github Actions when pushed to `main`
branch, you can check the latest image [here](https://hub.docker.com/repository/docker/marcosav/tam-crm-service).

## Environment Variables

The following environment variables can be set to configure the application:

| Property                | Description                                               | Info/Default value                                            |
|-------------------------|-----------------------------------------------------------|---------------------------------------------------------------|
| `LOG_LEVEL`             | Log level for the application                             | `INFO` and `DEBUG` for `local` profile                        |
| `DB_HOST`               | Database host                                             | `localhost`                                                   |
| `DB_PORT`               | Database port                                             | `5432`                                                        |
| `DB_NAME`               | Database name                                             | `crm`                                                         |
| `DB_PARAMS`             | Database connection parameters                            |                                                               |
| `DB_USERNAME`           | Database user                                             | `db-user`                                                     |
| `DB_PASSWORD`           | Database password                                         | `db-password`                                                 |
| `AWS_REGION`            | AWS region                                                | `eu-west-1` for `local` profile                               |
| `AWS_BUCKET`            | AWS bucket                                                | `crm-bucket` for `local` profile                              
| `AWS_ACCESS_KEY`        | AWS access key                                            | `secret` for `local` profile                                  |
| `AWS_SECRET_KEY`        | AWS secret key                                            | `secret` for `local` profile                                  |
| `AWS_ENDPOINT`          | AWS S3 endpoint                                           | `https://localhost.localstack.cloud:4566` for `local` profile |
| `AWS_EXTERNAL_ENDPOINT` | AWS S3 endpoint for signed URL (see `docker-compose.yml`) | `https://localhost.localstack.cloud:4566` for `local` profile |
| `JWT_SECRET`            | JWT secret key                                            | A random insecure secret is default set for `local` profile   |
| `JWT_EXPIRATION`        | JWT expiration time in seconds                            | `86400`                                                       |
| `CRM_URL_LIFETIME`      | Customer profile image URL expiration time in seconds     | `7200`                                                        |

### Other environment variables

`SPRING_PROFILES_ACTIVE` environment variable should be set to `local` for local deployment, check default values in the
table above. |
`SERVER_PORT` will set port where the application will be deployed, defaults to `8080`.
`SWAGGER_ENABLED` will enable/disable Swagger UI and docs, defaults to `false`, and `true` for `local` profile.

## API Specification

You will be able to access to the OpenAPI specification in the following URLs:

Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

OpenAPI Docs: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

NOTE: Check `SWAGGER_ENABLED` environment variable to enable/disable Swagger UI and docs.