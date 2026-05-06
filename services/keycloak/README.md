# keycloak

This folder contains the **Keycloak** setup for **Picshare**.

It uses a custom provider (User Storage SPI) packaged as a JAR and baked into the Keycloak image.

## What this service does

- Runs the Picshare identity provider (OIDC).
- Exposes realm endpoints used by backend services as JWT issuers.
- Includes a custom User Storage Provider plugin (`keycloak-storage-spi`).

## Image build

The `services/keycloak/Dockerfile` is a multi-stage build:

1. **Builder stage** based on `quay.io/keycloak/keycloak:latest`
   - Enables health + metrics
   - Configures `KC_DB=postgres`
   - Adds the provider JAR into `/opt/keycloak/providers/`
   - Generates a demo keystore
   - Runs `kc.sh build`

2. **Final stage** copies the built Keycloak distribution from the builder stage.

### Provider JAR requirement

The Dockerfile expects this file to exist at build time:

- `services/keycloak/providers/keycloak-storage-spi/target/keycloak-storage-spi-1.0-SNAPSHOT.jar`

Build it first:

```bash
cd services/keycloak/providers/keycloak-storage-spi
mvn clean package
```

## Custom provider: keycloak-storage-spi

Location:

- `services/keycloak/providers/keycloak-storage-spi`

Notes (from `pom.xml`):

- Java release: **21**
- Imports Keycloak parent BOM version **26.5.4**
- Produces a JAR: `keycloak-storage-spi-1.0-SNAPSHOT.jar`

## Run

How you run Keycloak depends on the repo’s docker-compose / stack setup, but typically you will:

1. Start Postgres (Keycloak DB)
2. Build the provider JAR
3. Build the Keycloak image
4. Run Keycloak with appropriate env vars (DB URL, user, password, etc.)

### Helpful endpoints

When running, services in this repo are configured to use the realm endpoints like:

- Issuer (local): `http://localhost:8090/realms/picshare-realm`
- JWK set (container): `http://keycloak:8080/realms/picshare-realm/protocol/openid-connect/certs`

> If you change ports/hostnames, update the corresponding `issuer-uri` / `jwk-set-uri` in the microservices.
