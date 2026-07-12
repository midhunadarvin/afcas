# Configuration

## Web / Spring properties

File: `app/src/main/resources/application.properties`

```properties
# Server
server.port=8080

# Database
afcas.database.host=localhost
afcas.database.port=5432
afcas.database.username=postgres
afcas.database.password=123456

# Thymeleaf
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

# Logging
logging.level.com.afcas=DEBUG
logging.level.org.springframework.web=INFO

# Schema auto-init
afcas.database.init.enabled=true
afcas.database.init.check-procedure=GetPrincipalList

# Authorization engine: sql | java
afcas.engine.mode=sql
```

## Property reference

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | `8080` | HTTP port for web UI |
| `afcas.database.host` | `localhost` | PostgreSQL host |
| `afcas.database.port` | `5432` | PostgreSQL port |
| `afcas.database.username` | `postgres` | DB user |
| `afcas.database.password` | `123456` | DB password |
| `afcas.database.init.enabled` | `true` | Auto-run schema scripts if missing |
| `afcas.database.init.check-procedure` | `GetPrincipalList` | Procedure used as init probe |
| `afcas.engine.mode` | `sql` | `sql` = stored procedures; `java` = app-layer engine |
| `logging.level.com.afcas` | `DEBUG` | Application log level |

## Overrides

### Command line

```bash
java -jar app.jar \
  --afcas.database.host=192.168.1.100 \
  --afcas.database.password=secret \
  --afcas.engine.mode=java
```

### Environment variables

Spring relaxed binding maps env vars, for example:

```bash
export AFCAS_DATABASE_HOST=192.168.1.100
export AFCAS_DATABASE_PASSWORD=secret
export AFCAS_ENGINE_MODE=java
java -jar app.jar
```

## CLI connection options

The PicoCLI top-level options (not Spring properties):

| Flag | Description |
|------|-------------|
| `-h`, `--host` | Database hostname |
| `-p`, `--port` | Database port |
| `-U`, `--username` | Database user |
| `-W`, `--password` | Prompt for password |

At runtime you can reconnect:

```text
connect-db -h localhost -p 5432 -U postgres -W
```

## Engine mode details

See [Engine Modes](/architecture/engine-modes).

- **`sql`** — `AuthorizationProvider` calls PostgreSQL functions/procedures.
- **`java`** — `AuthorizationEngine` + `EdgeManager` + JDBC repositories implement the same decisions in Java.

`EngineConfiguration` selects the `IAuthorizationProvider` bean based on `afcas.engine.mode`.
