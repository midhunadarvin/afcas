# Configuration Properties Reference

## Server

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | `8080` | Web UI port |

## Database

| Property | Default | Description |
|----------|---------|-------------|
| `afcas.database.host` | `localhost` | PostgreSQL host |
| `afcas.database.port` | `5432` | Port |
| `afcas.database.username` | `postgres` | User |
| `afcas.database.password` | `123456` | Password |

## Schema initialization

| Property | Default | Description |
|----------|---------|-------------|
| `afcas.database.init.enabled` | `true` | Auto-apply scripts if probe fails |
| `afcas.database.init.check-procedure` | `GetPrincipalList` | Existence probe |
| `afcas.database.init.scripts-location` | `classpath:db/init/*.postgres.sql` | Script glob (initializer) |

## Engine

| Property | Default | Description |
|----------|---------|-------------|
| `afcas.engine.mode` | `sql` | `sql` or `java` |

## Thymeleaf

| Property | Default |
|----------|---------|
| `spring.thymeleaf.cache` | `false` |
| `spring.thymeleaf.prefix` | `classpath:/templates/` |
| `spring.thymeleaf.suffix` | `.html` |

## Logging

| Property | Default |
|----------|---------|
| `logging.level.com.afcas` | `DEBUG` |
| `logging.level.org.springframework.web` | `INFO` |

## CLI flags (non-Spring)

| Flag | Meaning |
|------|---------|
| `-h` / `--host` | DB host |
| `-p` / `--port` | DB port |
| `-U` / `--username` | DB user |
| `-W` / `--password` | Password prompt |

## Docker Compose env

| Variable | Default |
|----------|---------|
| `POSTGRES_PASSWORD` | `123456` |
