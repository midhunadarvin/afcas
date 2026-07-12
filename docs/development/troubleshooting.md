# Troubleshooting

## Unsupported class file major version 65

Java 21 bytecode with an older Gradle. Upgrade the wrapper distribution in `gradle/wrapper/gradle-wrapper.properties` (e.g. Gradle 8.6+).

## SCRAM-based authentication failed

PostgreSQL requires a password. Set one:

```sql
ALTER USER postgres PASSWORD 'yourpassword';
```

Or align `afcas.database.password` / CLI `-W` with `pg_hba.conf`.

## Schema / procedure does not exist

Run `sql/[0-9]*.postgres.sql` in order, or enable web auto-init:

```properties
afcas.database.init.enabled=true
```

## Circular relation detected

You tried to add an edge that would create a cycle. Adjust hierarchy direction.

## Explicit denials are not supported

CLI only allows `Grant` when adding permissions. Do not pass `Deny` to `add permission`.

## Native image build fails

- Confirm GraalVM is active (`java -version` shows GraalVM)
- Install `native-image`
- Update reflection config for new dependencies

## Web UI cannot connect

Check:

1. PostgreSQL is running (`docker compose ps`)
2. `afcas.database.*` host/port/user/password
3. Firewall / Docker network (`host.docker.internal`)

## Documentation site

```bash
cd docs
npm install
npm run dev
```

If port 5173 is busy, VitePress will pick the next free port.
