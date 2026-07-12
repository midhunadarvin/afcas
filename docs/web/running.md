# Running the Web UI

The web UI is a **Spring Boot** application. Use `bootRun` for development or `bootJar` for a packaged executable.

## Prerequisites

- Java 17+
- PostgreSQL reachable with the credentials in `application.properties` (or overrides)
- Schema present **or** `afcas.database.init.enabled=true` (default) so scripts under `classpath:db/init/` can run on first start

Start PostgreSQL (example):

```bash
docker compose up -d
```

## Development — `bootRun`

```bash
./gradlew bootRun
# equivalent: ./gradlew :app:bootRun
```

Open **http://localhost:8080**

Override configuration:

```bash
./gradlew bootRun --args='--afcas.database.password=secret --afcas.engine.mode=java'
```

## Packaged JAR — `bootJar`

```bash
./gradlew bootJar
java -jar app/build/libs/afcas-web-1.0-SNAPSHOT.jar
```

With overrides:

```bash
java -jar app/build/libs/afcas-web-1.0-SNAPSHOT.jar \
  --server.port=8080 \
  --afcas.database.host=localhost \
  --afcas.database.password=secret \
  --afcas.engine.mode=sql
```

## Entry points

| Runtime | Main class | Gradle / artifact |
|---------|------------|-------------------|
| **Web UI** | `com.afcas.web.AfcasWebApplication` | `bootRun`, `bootJar` → `afcas-web-1.0-SNAPSHOT.jar` |
| **CLI** | `com.afcas.AfcasApp` | `:app:run`, `shadowJar` → `afcas.jar` |

Do not confuse `./gradlew run` (CLI) with `./gradlew bootRun` (web).

## Configuration

See [Configuration](/guide/configuration) for all `afcas.*` properties.

| Property | Default | Role |
|----------|---------|------|
| `server.port` | `8080` | HTTP port |
| `afcas.database.host` | `localhost` | PostgreSQL host |
| `afcas.database.port` | `5432` | Port |
| `afcas.database.username` | `postgres` | User |
| `afcas.database.password` | `123456` | Password |
| `afcas.database.init.enabled` | `true` | Auto-apply SQL scripts if schema missing |
| `afcas.engine.mode` | `sql` | `sql` or `java` authorization engine |

## Troubleshooting

### Connection refused to PostgreSQL

Start the database and check host/port/password. The UI can still start if schema init fails (errors are logged), but CRUD pages will fail until the DB is up.

### Wrong app starts (`afcas>` prompt)

You ran `:app:run` (CLI). Use **`bootRun`** for the web UI.

### Port 8080 in use

```bash
./gradlew bootRun --args='--server.port=8081'
```
