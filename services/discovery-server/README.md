# discovery-server (Eureka)

Service discovery for **Picshare**, implemented as a **Spring Cloud Netflix Eureka Server**.

- **App name:** `discovery-server`
- **Port:** `8761`
- **Eureka dashboard:** http://localhost:8761

## What this service does

This service runs a Eureka registry where other Picshare microservices can:

- register themselves (Eureka clients)
- discover other services by name
- perform client-side load balancing (depending on how clients are configured)

In this project, the discovery server is configured to run as a **standalone registry** (it does not register itself or fetch a registry from another server).

## Tech stack

- Java 21
- Spring Boot (parent: `spring-boot-starter-parent`)
- Spring Cloud Netflix Eureka Server (`spring-cloud-starter-netflix-eureka-server`)
- Spring Boot Actuator
- Micrometer Tracing (Brave) + Zipkin export

## Configuration (important defaults)

Key values from `src/main/resources/application.yaml`:

- Server port: `8761`
- Eureka:
  - `register-with-eureka: false`
  - `fetch-registry: false`
  - `enable-self-preservation: false`
  - Default zone: `http://localhost:8761/eureka`
- Actuator endpoints exposed: `health`, `info`
- Tracing:
  - sampling probability: `1` (always sample)
  - Zipkin endpoint: `http://zipkin:9411/api/v2/spans`

## Run locally (without Docker)

From the repository root:

```bash
cd services/discovery-server
./mvnw spring-boot:run
```

Then open:

- Eureka UI: http://localhost:8761
- Actuator health: http://localhost:8761/actuator/health
- Actuator info: http://localhost:8761/actuator/info

## Build and run the production container

The production `Dockerfile` expects a built JAR in `target/`.

From `services/discovery-server`:

```bash
# build jar
./mvnw clean package

# build image
docker build -t picshare-discovery-server .

# run
docker run --rm -p 8761:8761 picshare-discovery-server
```

## Development with Docker (hot reload loop)

`Dockerfile.dev` is set up to run `spring-boot:run` and restart on changes using `entr`.

From the **repository root** (important, because the Dockerfile copies `services/discovery-server/...` paths):

```bash
docker build -f services/discovery-server/Dockerfile.dev -t picshare-discovery-server-dev .
docker run --rm -p 8761:8761 picshare-discovery-server-dev
```

## Using this discovery server from other services

Typical Eureka client configuration (in another service) points to:

- `eureka.client.service-url.defaultZone: http://discovery-server:8761/eureka` (Docker network)
- or `http://localhost:8761/eureka` (local dev)

Once clients are running, you should see them appear on the Eureka dashboard.

## Observability / tracing notes

This service is configured to export traces to Zipkin:

- `http://zipkin:9411/api/v2/spans`

If you are not running Zipkin, you may want to:
- start Zipkin alongside your stack, or
- disable tracing export / adjust the endpoint for your environment.

---
Project: `the-nap/picshare` → `services/discovery-server`