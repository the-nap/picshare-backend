# post-service

Microservice responsible for **creating and managing posts** in **Picshare**.

- **Service name:** `post-service`
- **Service discovery:** Eureka client (registers to `discovery-server`)
- **Default port:** `8080`

## What this service does

This service exposes endpoints for creating posts (multipart upload is enabled) and interacts with other services via Kafka events.

It also consumes user lifecycle events (e.g., user deletion) to keep its data consistent.

## Tech stack

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- PostgreSQL driver
- Spring Security (OAuth2 Resource Server / JWT)
- Spring Cloud Stream + Kafka binder
- Eureka client (`spring-cloud-starter-netflix-eureka-client`)
- Observability: Actuator + Micrometer Tracing (Brave) + Zipkin
- MapStruct

## Configuration (defaults)

Key values from `src/main/resources/application.yaml`:

- `spring.application.name`: `post-service`
- `server.port`: `8080`
- Multipart:
  - `spring.servlet.multipart.max-file-size`: `10MB`
  - `spring.servlet.multipart.max-request-size`: `10MB`
- Eureka defaultZone: `http://discovery-server:8761/eureka`
- OAuth2 / JWT:
  - issuer: `http://localhost:8090/realms/picshare-realm`
  - JWK set (container): `http://keycloak:8080/realms/picshare-realm/protocol/openid-connect/certs`
- Kafka broker (container): `broker:29092`
- Actuator exposed: `health`, `info`, `metrics`
- Zipkin endpoint: `http://zipkin:9411/api/v2/spans`

## Kafka integration

Spring Cloud Function definition:

```yaml
spring.cloud.function.definition: postSaveSuccess;userDeleted
```

Bindings/topics (in `application.yaml`):

- **Consumes** `post_save_success_topic`
- **Consumes** `post_save_failure_topic`
- **Publishes** `post_confirmed_topic`
- **Consumes** `user_deleted_topic`
- **Publishes** `post_deleted_topic`

> Note: The binding names include both `-in-0` and `-out-0` depending on direction.

## Run locally (no Docker)

From repo root:

```bash
cd services/post-service
./mvnw spring-boot:run
```

## Build & run (production container)

From `services/post-service`:

```bash
./mvnw clean package

docker build -t picshare-post-service .
docker run --rm -p 8080:8080 picshare-post-service
```

## Development container (auto-restart)

From **repo root**:

```bash
docker build -f services/post-service/Dockerfile.dev -t picshare-post-service-dev .
docker run --rm -p 8080:8080 picshare-post-service-dev
```

## Health/metrics

- Health: `GET /actuator/health`
- Info: `GET /actuator/info`
- Metrics: `GET /actuator/metrics`
