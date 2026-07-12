# Prerequisites

## Required

| Tool | Version | Purpose |
|------|---------|---------|
| **Java** | 17+ (toolchain set to 17; docs also mention 21) | Runtime and compile |
| **PostgreSQL** | 14+ | Authorization store |
| **Gradle** | Wrapper included (`./gradlew`) | Build |

## Optional

| Tool | Purpose |
|------|---------|
| **GraalVM** + `native-image` | Native executable builds |
| **Docker** / Docker Compose | Local PostgreSQL and containerized app |
| **Node.js 18+** | Running this VitePress documentation site |

## Install Java (example)

```bash
# SDKMAN
sdk install java 17-open
# or
sdk install java 21-open

java -version
```

## Install PostgreSQL

Use your platform package manager, a cloud instance, or Docker:

```bash
docker compose up -d
# Starts postgres:14.4 on port 5432, password default 123456
```

## Verify Gradle wrapper

```bash
./gradlew --version
```

No global Gradle install is required.

## Documentation site (this site)

```bash
cd docs
npm install
npm run dev
```

Open the URL printed by VitePress (typically `http://localhost:5173`).
