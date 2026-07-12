# Web Application Overview

The web UI is a **Spring Boot** application with **Thymeleaf** server-side rendering. It wraps the same authorization managers for interactive administration.

## Features

- Dashboard home
- Manage principals and group membership
- Manage operations and resources
- Grant/remove permissions
- Run authorization checks and digests

## Stack

| Layer | Technology |
|-------|------------|
| Framework | Spring Boot 3.1 (Web, JDBC, Validation) |
| Templates | Thymeleaf + Bootstrap 5 (CDN) |
| Database | PostgreSQL via JDBC (`DatabaseHelper` + Spring `DataSource`) |
| Config | `application.properties` |
| Auth engines | SQL procedures or Java engine (`afcas.engine.mode`) |

## How to run

```bash
./gradlew bootRun
# → http://localhost:8080
```

Details: [Running the Web UI](./running).

## Startup sequence

1. `AfcasWebApplication` starts Spring (component scan: `com.afcas`)
2. `DatabaseConfig` initializes `DatabaseHelper` and exposes a `DataSource`
3. `DatabaseSchemaInitializer` optionally applies `db/init/*.postgres.sql`
4. `EngineConfiguration` selects SQL vs Java `IAuthorizationProvider`
5. Controllers serve Thymeleaf pages on port `8080`

## Main classes

| Role | Class |
|------|-------|
| Web | `com.afcas.web.AfcasWebApplication` |
| CLI | `com.afcas.AfcasApp` |
