# AFCAS Build and Run Guide

This document explains how to build and run AFCAS (A Fairly Capable Authorization Sub-System), including both the CLI tool and the Spring Boot web application.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Database Setup](#database-setup)
- [Building the Project](#building-the-project)
- [Running the CLI Tool](#running-the-cli-tool)
- [Running the Spring Web Application](#running-the-spring-web-application)
- [Docker Deployment](#docker-deployment)
- [Development](#development)

---

## Prerequisites

- **Java 21** (or Java 17+)
- **Gradle** (wrapper included via `./gradlew`)
- **PostgreSQL 14+** with a database created
- **GraalVM** (optional, for native image builds)

### Install Java

```bash
# Example using SDKMAN
sdk install java 21-open
```

### Verify Java Version

```bash
java -version
# Should show Java 21 or 17+
```

---

## Database Setup

### 1. Create a PostgreSQL Database

```bash
# Connect to PostgreSQL
sudo -u postgres psql

# Create database
CREATE DATABASE afcas;

# Connect to the database
\c afcas
```

### 2. Run SQL Initialization Scripts

Execute all scripts in the `sql/` directory **in order**:

```bash
psql -U postgres -d afcas -f sql/0-init-tables.postgres.sql
psql -U postgres -d afcas -f sql/1-edge-exists-function.postgres.sql
psql -U postgres -d afcas -f sql/2-add-edge-procedure.postgres.sql
psql -U postgres -d afcas -f sql/3-remove-edge-procedures.postgres.sql
psql -U postgres -d afcas -f sql/4-principal-procedures.postgres.sql
psql -U postgres -d afcas -f sql/5-operation-procedures.postgres.sql
psql -U postgres -d afcas -f sql/6-resource-procedures.postgres.sql
psql -U postgres -d afcas -f sql/7-access-predicate-procedures.postgres.sql
```

Or run all at once:

```bash
for f in sql/[0-9]*.postgres.sql; do psql -U postgres -d afcas -f "$f"; done
```

> **Note:** The web application can auto-initialize the schema if `afcas.database.init.enabled=true` in `application.properties`.

---

## Building the Project

The project uses Gradle with multiple build plugins for different artifact types.

### Quick Build (All Artifacts)

```bash
cd /testbed/afcas
./gradlew build --no-daemon
```

### Build Specific Artifacts

| Artifact Type | Command | Output Location |
|---------------|---------|-----------------|
| **Shadow JAR** (CLI) | `./gradlew shadowJar` | `app/build/libs/afcas.jar` |
| **Spring Boot JAR** (Web) | `./gradlew bootJar` | `app/build/libs/afcas-web-1.0-SNAPSHOT.jar` |
| **Plain JAR** | `./gradlew :app:jar` | `app/build/libs/*-plain.jar` |
| **Native Image** (CLI, GraalVM) | `./gradlew nativeCompile` | `app/build/native/nativeCompile/afcas` |

### Examples

```bash
# CLI fat JAR
./gradlew shadowJar

# Web executable JAR
./gradlew bootJar

# Native CLI (requires GraalVM)
./gradlew nativeCompile
```

---

## Running the CLI Tool

The CLI is built on **PicoCLI** with **JLine2** for interactive shell support.

### Run from Built JAR

```bash
# Using shadow JAR (recommended)
java -jar app/build/libs/afcas.jar

# Or via Gradle (application plugin)
./gradlew :app:run --args='-h localhost -p 5432 -U postgres'
```

### Command-Line Options

```bash
java -jar afcas.jar [OPTIONS] [COMMAND]

Options:
  -h, --host <HOSTNAME>     Database host name
  -p, --port <PORT>         Database port
  -U, --username <USER>     Database user name
  -W, --password            Prompt for password

Commands (subcommands):
  connect-db    Connect to a database
  add           Add principals, operations, resources, etc.
  remove        Remove principals, operations, resources, etc.
  get           Query lists and authorization info
  is-authorized Check authorization
  is-member-of  Check group membership
  cls / clear   Clear the screen
  help          Show help
```

### Interactive Usage Example

```bash
$ java -jar app/build/libs/afcas.jar
Enter value for --hostname:  localhost
Enter value for --port:  5432
Enter value for --username:  postgres
Enter value for --password:  ********
Database connected successfully!
afcas> add principal John User John john@example.com Developer manual
afcas> add operation Edit Edit Edit resources
afcas> add resource Workspace Workspace
afcas> connect-db -h localhost -p 5432 -U postgres -W
afcas> is-authorized John Edit Workspace
afcas> exit
```

### Direct Command Usage

```bash
# Connect and run a single command
java -jar afcas.jar -h localhost -p 5432 -U postgres -W

# Then at the prompt:
afcas> add principal Admin Group
afcas> add operation Read Read Read access
afcas> is-authorized Admin Read Workspace
```

---

## Running the Spring Web Application

```bash
# Development
./gradlew bootRun
# Open http://localhost:8080

# Packaged
./gradlew bootJar
java -jar app/build/libs/afcas-web-1.0-SNAPSHOT.jar
```

Configuration defaults (`app/src/main/resources/application.properties`):

```properties
server.port=8080
afcas.database.host=localhost
afcas.database.port=5432
afcas.database.username=postgres
afcas.database.password=123456
afcas.database.init.enabled=true
afcas.engine.mode=sql
```

Overrides:

```bash
./gradlew bootRun --args='--afcas.database.password=mypass --afcas.engine.mode=java'
# or
java -jar app/build/libs/afcas-web-1.0-SNAPSHOT.jar --afcas.database.password=mypass
```

---

## Docker Deployment

### Using Docker Compose

A `docker-compose.yaml` is provided for PostgreSQL:

```bash
docker-compose up -d
```

This starts PostgreSQL on port 5432 with password `123456`.

### Build Docker Image (Native)

```bash
docker build -t afcas-native .
docker run -it afcas-native
```

### Build and Run in Docker (JVM)

```bash
# Build the CLI fat JAR
./gradlew shadowJar

# Run with Docker (example — adjust image/entrypoint as needed)
docker run -it --rm \
  -e AFCAS_DATABASE_HOST=host.docker.internal \
  -v "$PWD/app/build/libs:/app" \
  eclipse-temurin:17-jre \
  java -jar /app/afcas.jar
```

---

## Development

### Project Structure

```
afcas/
├── app/
│   ├── build.gradle          # Gradle build config
│   ├── src/main/java/
│   │   ├── com/afcas/
│   │   │   ├── AfcasApp.java           # CLI entry point
│   │   │   ├── CliCommands.java        # CLI command definitions
│   │   │   ├── commands/               # CLI subcommands
│   │   │   ├── engine/                 # Auth engine (SQL/Java modes)
│   │   │   ├── impl/                   # AuthorizationManager, Provider
│   │   │   ├── web/                    # Spring Boot web app
│   │   │   │   ├── AfcasWebApplication.java
│   │   │   │   ├── controller/         # Web controllers
│   │   │   │   ├── config/             # DB config, schema init
│   │   │   │   └── templates/          # Thymeleaf HTML templates
│   │   │   └── utils/                  # DatabaseHelper, etc.
│   │   └── resources/
│   │       ├── application.properties  # Spring config
│   │       └── db/init/                # Embedded SQL scripts
│   └── libs/                   # Bundled JARs (e.g., postgresql driver)
├── sql/                        # PostgreSQL stored procedures
├── gradlew                     # Gradle wrapper
└── ARCHITECTURE.md             # Architecture documentation
```

### Key Gradle Tasks

| Task | Description |
|------|-------------|
| `./gradlew build` | Full build (compile, test, package) |
| `./gradlew compileJava` | Compile Java sources |
| `./gradlew test` | Run unit tests |
| `./gradlew bootRun` | Run Spring Boot web UI |
| `./gradlew bootJar` | Build web executable JAR |
| `./gradlew :app:run` | Run CLI main class |
| `./gradlew shadowJar` | Build fat CLI JAR |
| `./gradlew nativeCompile` | Build GraalVM native CLI image |
| `./gradlew clean` | Clean build artifacts |

### Running Tests

```bash
./gradlew test
```

Test reports: `app/build/reports/tests/test/index.html`

---

## Troubleshooting

### "Unsupported class file major version 65"

You're using Java 21 with an older Gradle. Upgrade Gradle wrapper:

```bash
# Edit gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.6-bin.zip
```

### "SCRAM-based authentication" error

PostgreSQL requires a password. Set one:

```sql
ALTER USER postgres PASSWORD 'yourpassword';
```

Or configure `pg_hba.conf` for local trust auth (dev only).

### Native image build fails

Ensure GraalVM is installed and on PATH:

```bash
gu install native-image
```

---

## License

See [LICENSE](LICENSE) file.
