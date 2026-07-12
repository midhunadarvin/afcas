# Database Setup

AFCAS persists all authorization data in PostgreSQL. Schema and logic live in ordered SQL scripts under `sql/` (and a mirrored copy under `app/src/main/resources/db/init/` for auto-init).

## Create the database

```bash
psql -U postgres -c "CREATE DATABASE afcas;"
```

The JDBC URL used by the app connects to the server root URL without a database name path in some configs — ensure your PostgreSQL default database / connection string matches how you deploy. For local work, connecting as `postgres` to a dedicated `afcas` database is recommended when running scripts via `psql -d afcas`.

## Run initialization scripts

Execute **in numeric order**:

| Order | File | Purpose |
|------:|------|---------|
| 0 | `0-init-tables.postgres.sql` | Core tables: Principal, Operation, Resource, AccessPredicate, Edge |
| 1 | `1-edge-exists-function.postgres.sql` | Edge existence helpers |
| 2 | `2-add-edge-procedure.postgres.sql` | `AddEdgeWithSpaceSavings` (transitive closure insert) |
| 3 | `3-remove-edge-procedures.postgres.sql` | Edge removal / cleanup |
| 4 | `4-principal-procedures.postgres.sql` | Principal CRUD, members list |
| 5 | `5-operation-procedures.postgres.sql` | Operation CRUD, sub-operations |
| 6 | `6-resource-procedures.postgres.sql` | Resource CRUD, sub-resources |
| 7 | `7-access-predicate-procedures.postgres.sql` | Permissions + `IsAuthorized` and related queries |

```bash
for f in sql/[0-9]*.postgres.sql; do
  psql -U postgres -d afcas -f "$f"
done
```

Test scripts (optional) live under `sql/test/`.

## Auto-initialization (web app)

When the Spring Boot web application starts:

- Property: `afcas.database.init.enabled=true` (default)
- Check: looks for procedure named by `afcas.database.init.check-procedure` (default `GetPrincipalList`)
- If missing: runs `classpath:db/init/*.postgres.sql` in order

Disable for production when schema is managed externally:

```properties
afcas.database.init.enabled=false
```

## Tables overview

See [Database Schema](/architecture/database-schema) for column-level detail.

- **Principal** — users and groups
- **Operation** — actions
- **Resource** — protected objects
- **AccessPredicate** — grants/denies
- **Edge** — hierarchy graph with hops

## Docker Compose database

```yaml
# docker-compose.yaml (excerpt)
services:
  db:
    image: postgres:14.4
    ports:
      - "5432:5432"
    environment:
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:-123456}
```

```bash
docker compose up -d
```

You still need to create an application database and run scripts (or use web auto-init against the appropriate database).
