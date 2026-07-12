# Web Application Overview

The web UI is a **Spring Boot** application with **Thymeleaf** server-side rendering. It wraps the same authorization managers/services as the CLI for interactive administration.

## Features

- Dashboard home
- Manage principals and group membership
- Manage operations and resources
- Grant/remove permissions
- Run authorization checks and digests

## Stack

| Layer | Tech |
|-------|------|
| Framework | Spring Boot |
| Templates | Thymeleaf (`templates/*.html`) |
| CSS | Bootstrap 5 (templates) |
| DB access | JDBC via `DatabaseHelper` + Spring `DataSource` |
| Config | `application.properties` |

## Startup behavior

1. `DatabaseConfig` initializes `DatabaseHelper` from `afcas.database.*`
2. Exposes a `DataSource` bean for JDBC repositories and schema init
3. `DatabaseSchemaInitializer` optionally applies `db/init/*.postgres.sql`
4. `EngineConfiguration` selects SQL vs Java provider

## Main class

`com.afcas.web.AfcasWebApplication`
