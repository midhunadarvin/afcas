# Running the Web UI

## Prerequisites

- PostgreSQL reachable with configured credentials
- Schema present **or** `afcas.database.init.enabled=true`

## Dev mode

```bash
./gradlew bootRun
```

Open http://localhost:8080

## From JAR

```bash
./gradlew bootJar
java -jar app/build/libs/app-1.0-SNAPSHOT.jar
```

Override config:

```bash
java -jar app.jar \
  --server.port=8080 \
  --afcas.database.host=localhost \
  --afcas.database.password=secret \
  --afcas.engine.mode=sql
```

## Configuration recap

See [Configuration](/guide/configuration) for all `afcas.*` properties.
