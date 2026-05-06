# user-service

Microservice responsible for **user management** in **Picshare**.

It publishes user-related events to Kafka and validates requests using **JWTs issued by Keycloak**.

- **Service name:** `user-service`
- **Service discovery:** Eureka client (registers to `discovery-server`)
- **Default port:** `8080`

## What this service does

- Manages user data (persisted via JPA).
- Emits events when users are deleted.
- Emits events for user connection lifecycle (create/delete connection).

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

- `spring.application.name`: `user-service`
- `server.port`: `8080`
- Multipart:
  - `max-file-size`: `5MB`
  - `max-request-size`: `5MB`
- Eureka defaultZone: `http://discovery-server:8761/eureka`
- OAuth2 / JWT:
  - issuer: `http://localhost:8090/realms/picshare-realm`
  - JWK set (container): `http://keycloak:8080/realms/picshare-realm/protocol/openid-connect/certs`
- Kafka broker (container): `broker:29092`
- Actuator exposed: `health`, `info`, `metrics`
- Zipkin endpoint: `http://zipkin:9411/api/v2/spans`

## Kafka integration

Bindings/topics (in `application.yaml`):

- **Publishes** `user_deleted_topic`
- **Publishes** `connection_created_topic`
- **Publishes** `connection_deleted_topic`

## Run locally (no Docker)

From repo root:

```bash
cd services/user-service
./mvnw spring-boot:run
```

## Build & run (production container)

From `services/user-service`:

```bash
./mvnw clean package

docker build -t picshare-user-service .
docker run --rm -p 8080:8080 picshare-user-service
```

## Development container (auto-restart)

From **repo root**:

```bash
docker build -f services/user-service/Dockerfile.dev -t picshare-user-service-dev .
docker run --rm -p 8080:8080 picshare-user-service-dev
```

## Health/metrics

- Health: `GET /actuator/health`
- Info: `GET /actuator/info`
- Metrics: `GET /actuator/metrics`
