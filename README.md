# TAM CRM Service

## Description

This is a customer management service made in Java.

The project is based on a Hexagonal Architecture (using Ports and Adapters pattern) & DDD (Domain Driven Design).
Made following an API First approach (more info about it below).

Persistence is achieved using a PostgreSQL database for user and customer data, and S3 for customer profile images.

## Requirements

- JDK 21 & Maven
- Lombok plugin (installation depends on IDE)
- Docker (Integration Tests & Deployment)
- [google-java-format](https://github.com/google/google-java-format) plugin

## Code format

This project follows [Google Java Style](https://google.github.io/styleguide/javaguide.html) conventions.

## Development

To start setting things up execute the following, this will generate needed mappers and APIs (model & controller),
compile the project and execute tests:

```
mvn clean install
```

To execute the application (locally), using `local` Spring profile (`SPRING_PROFILES_ACTIVE`):

```
mvn spring-boot:run
```

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