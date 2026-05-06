# storage-service

Microservice responsible for **object storage** in **Picshare**.

It integrates with **MinIO** for storage and listens/publishes events on Kafka for post lifecycle workflows.

- **Service name:** `storage-service`
- **Service discovery:** Eureka client (registers to `discovery-server`)
- **Default port:** `8080`

## What this service does

- Stores and retrieves media (images) via an S3-compatible API (MinIO).
- Produces success/failure events for post media handling.
- Reacts to deletion events to remove associated media.

## Tech stack

- Java 21
- Spring Boot
- Spring Web MVC
- MinIO Java SDK
- Image processing libraries:
  - `webp-imageio`
  - `imgscalr`
- Spring Cloud Stream + Kafka binder
- Eureka client (`spring-cloud-starter-netflix-eureka-client`)
- Observability: Actuator + Micrometer Tracing (Brave) + Zipkin

## Configuration (defaults)

Key values from `src/main/resources/application.yaml`:

- `spring.application.name`: `storage-service`
- `server.port`: `8080`
- Multipart:
  - `spring.servlet.multipart.max-file-size`: `10MB`
  - `spring.servlet.multipart.max-request-size`: `10MB`
- MinIO:
  - `minio.url`: `http://minio:9000`
  - `minio.access.name`: `minioadmin`
  - `minio.access.secret`: `minioadmin`
  - `minio.bucket.name`: `data`
- Eureka defaultZone: `http://discovery-server:8761/eureka`
- Kafka broker (container): `broker:29092`
- Actuator exposed: `health`, `info`, `metrics`
- Zipkin endpoint: `http://zipkin:9411/api/v2/spans`

## Kafka integration

Spring Cloud Function definition:

```yaml
spring.cloud.function.definition: postDeleted; userDeleted
```

Bindings/topics (in `application.yaml`):

- **Consumes** `post_deleted_topic`
- **Consumes** `user_deleted_topic`
- **Publishes** `post_save_success_topic`
- **Publishes** `post_save_failure_topic`

## Run locally (no Docker)

From repo root:

```bash
cd services/storage-service
./mvnw spring-boot:run
```

## Build & run (production container)

From `services/storage-service`:

```bash
./mvnw clean package

docker build -t picshare-storage-service .
docker run --rm -p 8080:8080 picshare-storage-service
```

## Development container (auto-restart)

From **repo root**:

```bash
docker build -f services/storage-service/Dockerfile.dev -t picshare-storage-service-dev .
docker run --rm -p 8080:8080 picshare-storage-service-dev
```

## Health/metrics

- Health: `GET /actuator/health`
- Info: `GET /actuator/info`
- Metrics: `GET /actuator/metrics`
