# feed-service

Microservice responsible for creating and serving a user’s **personal feed** in **Picshare**.

- **Service name:** `feed-service`
- **Service discovery:** Eureka client (registers to `discovery-server`)
- **Default port:** `8080`

## What this service does

This service aggregates/produces a personalized feed for users.

It also consumes domain events from Kafka to keep its read model in sync (see **Kafka topics** below).

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
- MapStruct (mapping)

## Configuration (defaults)

Key values from `src/main/resources/application.yaml`:

- `spring.application.name`: `feed-service`
- `server.port`: `8080`
- Eureka defaultZone: `http://discovery-server:8761/eureka`
- OAuth2 / JWT:
  - issuer: `http://localhost:8090/realms/picshare-realm`
  - JWK set (container): `http://keycloak:8080/realms/picshare-realm/protocol/openid-connect/certs`
- Kafka broker (container): `broker:29092`
- Actuator exposed: `health`, `info`, `metrics`
- Zipkin endpoint: `http://zipkin:9411/api/v2/spans`

## Kafka integration

This service is configured as a **Spring Cloud Function** consumer with:

```yaml
spring.cloud.function.definition: userDeleted; postDeleted; connectionCreated; connectionDeleted; postConfirmed
```

Bindings/topics (in `application.yaml`):

- **Consumes** `user_deleted_topic`
- **Consumes** `post_deleted_topic`
- **Consumes** `connection_created_topic`
- **Consumes** `connection_deleted_topic`
- **Consumes** `post_confirmed_topic`

Each consumer uses group: `${spring.application.name}`.

## Run locally (no Docker)

From repo root:

```bash
cd services/feed-service
./mvnw spring-boot:run
```

## Build & run (production container)

From `services/feed-service`:

```bash
./mvnw clean package

docker build -t picshare-feed-service .
docker run --rm -p 8080:8080 picshare-feed-service
```

## Development container (auto-restart)

`Dockerfile.dev` runs `spring-boot:run` and restarts on changes using `entr`.

From **repo root**:

```bash
docker build -f services/feed-service/Dockerfile.dev -t picshare-feed-service-dev .
docker run --rm -p 8080:8080 picshare-feed-service-dev
```

## Health/metrics

- Health: `GET /actuator/health`
- Info: `GET /actuator/info`
- Metrics: `GET /actuator/metrics`
