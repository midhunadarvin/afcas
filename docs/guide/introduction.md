# Introduction

**AFCAS** (*A Fairly Capable Authorization Sub-System*) is a Java / PostgreSQL port of the classic [CodeProject authorization design](https://www.codeproject.com/Articles/30380/A-Fairly-Capable-Authorization-Sub-System-with-Row). It provides flexible, hierarchical role-based access control (RBAC) with row-level style permission checks.

## What AFCAS manages

| Concept | Description |
|---------|-------------|
| **Principals** | Users and groups that can hold permissions |
| **Operations** | Actions that can be performed (e.g. `Read`, `Edit`) |
| **Resources** | Entities being protected (e.g. `Workspace`, `Document123`) |
| **Access predicates** | Grant (or Deny) of an operation on a resource to a principal |
| **Edges** | Hierarchical relationships with pre-computed transitive closure |

## Interfaces

1. **CLI** — PicoCLI interactive shell (`afcas.jar` / native binary). Primary interface documented in the original README.
2. **Web UI** — Spring Boot + Thymeleaf SSR app on port `8080`.
3. **SQL** — Call stored procedures directly against PostgreSQL when needed.

## High-level flow

```
┌──────────────────┐     ┌──────────────────┐
│  CLI (PicoCLI)   │     │  Web (Spring)    │
└────────┬─────────┘     └────────┬─────────┘
         │                        │
         └───────────┬────────────┘
                     ▼
         ┌───────────────────────┐
         │ AuthorizationManager  │  CRUD
         │ AuthorizationProvider │  decisions / queries
         └───────────┬───────────┘
                     ▼
         ┌───────────────────────┐
         │ PostgreSQL            │
         │ tables + procedures   │
         │ (or Java engine)      │
         └───────────────────────┘
```

## Design goals

- **Performance** — Transitive closure is stored in the `Edge` table so authorization checks do not need recursive walks at query time.
- **Flexibility** — One graph model serves principals, operations, and resources.
- **Safety** — Circular relationships are rejected when edges are added.
- **Portability path** — Optional Java engine mode moves logic out of stored procedures for testing and multi-DB scenarios.

## Next steps

- [Prerequisites](./prerequisites) — install Java, PostgreSQL, tools
- [Quick Start](./quick-start) — get a working environment in minutes
- [Authorization Model](/concepts/authorization-model) — understand how decisions work
