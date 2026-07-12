# Testing

## Run tests

```bash
./gradlew test
```

Reports: `app/build/reports/tests/test/index.html`

## Existing tests

Under `app/src/test/java/com/afcas/`:

- `AfcasAppTest`
- `impl/AuthorizationManagerTest`
- `utils/ObjectCache`

## Suggested coverage (engine work)

When working on Java engine mode:

1. Unit test `EdgeManager` with an in-memory `EdgeRepository`
2. Unit test `AuthorizationEngine.isAuthorized` hierarchy cases
3. Integration tests comparing `sql` vs `java` mode results

Example scenario:

```text
john ∈ admins ∈ superusers
admins has Grant Edit on Workspace
→ isAuthorized(john, Edit, Workspace) == true
```

## Test SQL

Optional scripts: `sql/test/0-edge-test.postgres.sql`, `sql/test/1-principal-test.postgres.sql`.
