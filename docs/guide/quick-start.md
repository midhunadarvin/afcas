# Quick Start

This guide gets AFCAS running with PostgreSQL and the CLI (or web UI).

## 1. Start PostgreSQL

```bash
# From the repository root
docker compose up -d
```

Default credentials (from `docker-compose.yaml` and `application.properties`):

- Host: `localhost`
- Port: `5432`
- User: `postgres`
- Password: `123456` (override with `POSTGRES_PASSWORD`)

## 2. Create database and load schema

```bash
# Create database (if not using auto-init via web app)
createdb -U postgres afcas
# or: psql -U postgres -c 'CREATE DATABASE afcas;'

# Run SQL scripts in order
for f in sql/[0-9]*.postgres.sql; do
  psql -U postgres -d afcas -f "$f"
done
```

> **Web app shortcut:** With `afcas.database.init.enabled=true` (default), the Spring web app can auto-run scripts from `classpath:db/init/` if procedures are missing.

## 3. Build the CLI fat JAR

```bash
./gradlew shadowJar
# Output: app/build/libs/afcas.jar
```

## 4. Run the CLI

```bash
java -jar app/build/libs/afcas.jar -h localhost -p 5432 -U postgres -W
```

Or start without flags and answer the interactive prompts for host, port, username, and password.

## 5. Create a minimal authorization graph

At the `afcas>` prompt:

```text
add principal Admin Group
add principal John User John john@example.com Developer manual
add group-member Admin John
add operation Edit Edit Edit resources
add resource Workspace Workspace
add permission Admin Edit Workspace Grant
is-authorized John Edit Workspace
```

Expected: John is authorized because he is a member of Admin, which holds the Grant.

## 6. (Optional) Start the Web UI

If your build includes the Spring Boot entry point:

```bash
./gradlew bootRun
# or
java -jar app/build/libs/app-1.0-SNAPSHOT.jar
```

Open **http://localhost:8080**.

## What to read next

- [CLI Overview](/cli/overview) — full command reference
- [Configuration](./configuration) — database and engine mode properties
- [Hierarchy & Edges](/concepts/hierarchy-and-edges) — how inheritance works
