# SQL Scripts Reference

## Application schema (`sql/`)

| File | Contents |
|------|----------|
| `0-init-tables.postgres.sql` | Tables + indexes |
| `1-edge-exists-function.postgres.sql` | Edge existence |
| `2-add-edge-procedure.postgres.sql` | Add edge + transitive closure |
| `3-remove-edge-procedures.postgres.sql` | Remove edge / related |
| `4-principal-procedures.postgres.sql` | Principal procedures |
| `5-operation-procedures.postgres.sql` | Operation procedures |
| `6-resource-procedures.postgres.sql` | Resource procedures |
| `7-access-predicate-procedures.postgres.sql` | Predicates + auth functions |

## Embedded copy

`app/src/main/resources/db/init/` mirrors the same numbered scripts for classpath loading by `DatabaseSchemaInitializer`.

## Tests

| File | Purpose |
|------|---------|
| `sql/test/0-edge-test.postgres.sql` | Edge tests |
| `sql/test/1-principal-test.postgres.sql` | Principal tests |

## Apply all

```bash
for f in sql/[0-9]*.postgres.sql; do
  psql -U postgres -d afcas -f "$f"
done
```
