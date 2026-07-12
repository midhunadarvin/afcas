# System Overview

## Layered architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Application Layer                        │
├─────────────────────────┬───────────────────────────────────────┤
│   Web GUI (Spring Boot) │           CLI (PicoCLI)               │
│   - Thymeleaf SSR       │           - Interactive Shell         │
│   - Controllers         │           - Scriptable commands       │
└────────────┬────────────┴───────────────────────┬───────────────┘
             │                                    │
             └───────────────┬────────────────────┘
                             ▼
┌────────────────────────────────────────────────────────────────┐
│                    Service / Domain Layer                      │
│  IAuthorizationManager  │  IAuthorizationProvider              │
│  AuthorizationManager   │  AuthorizationProvider (SQL)         │
│                         │  AuthorizationEngine (Java)          │
│  EdgeManager + JDBC repositories (java mode)                   │
└────────────────────────────┬───────────────────────────────────┘
                             ▼
┌────────────────────────────────────────────────────────────────┐
│                 Database Layer (PostgreSQL)                    │
│  Tables + Procedures / Functions                               │
└────────────────────────────────────────────────────────────────┘
```

## Package map (`com.afcas`)

| Package | Responsibility |
|---------|----------------|
| `commands` | PicoCLI subcommands |
| `web` | Spring Boot app, controllers, templates, DB config |
| `impl` | `AuthorizationManager`, `AuthorizationProvider` |
| `engine` | Java-mode `AuthorizationEngine`, `EdgeManager` |
| `repository` / `repository.jdbc` | Persistence for java mode |
| `objects` | Domain types and interfaces |
| `factory` | Manager factory for CLI |
| `utils` | `DatabaseHelper`, stored procedure executor |
| `config` | Engine mode Spring configuration |

## Technology stack

| Concern | Technology |
|---------|------------|
| Language | Java 17+ |
| Build | Gradle 8.x, Shadow, GraalVM Native |
| CLI | PicoCLI, JLine2 |
| Web | Spring Boot, Thymeleaf, Bootstrap (templates) |
| DB | PostgreSQL 14+, JDBC |
| Auth logic | PL/pgSQL procedures and/or Java engine |

## Entry points

| Entry | Class | Gradle / artifact |
|-------|-------|-------------------|
| CLI | `com.afcas.AfcasApp` | `shadowJar` → `afcas.jar`, `:app:run` |
| Web UI | `com.afcas.web.AfcasWebApplication` | `bootRun`, `bootJar` → `afcas-web-*.jar` |

## Data flow (CLI add principal)

1. User types `add principal ...`
2. `AddPrincipal` builds `Principal` and calls `AuthorizationManagerFactory.getInstance()`
3. Manager executes stored procedure via `DatabaseHelper` / executor
4. PostgreSQL upserts row in `Principal`
