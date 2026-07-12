## AFCAS

A Java / PostgreSQL port of **A Fairly Capable Authorization Sub-System**.

Based on: [CodeProject article](https://www.codeproject.com/Articles/30380/A-Fairly-Capable-Authorization-Sub-System-with-Row)

### Documentation

Full documentation is maintained as a **VitePress** site in the `docs/` directory.

```bash
cd docs
npm install
npm run dev
```

Then open the URL shown in the terminal (default [http://localhost:5173](http://localhost:5173)).

| Command | Description |
|---------|-------------|
| `npm run dev` | Local documentation server with hot reload |
| `npm run build` | Static production build to `docs/.vitepress/dist` |
| `npm run preview` | Preview the production build |

Start here in the docs:

- **Introduction & quick start** — how the system works and how to run it
- **Configuration** — database, engine mode (`sql` / `java`), schema auto-init
- **CLI reference** — all interactive shell commands
- **Architecture** — schema, layers, stored procedures, Java engine

Historical long-form notes also remain in the repo root (`ARCHITECTURE.md`, `BUILD_AND_RUN.md`). Prefer the VitePress site for day-to-day use.

### Quick start (application)

**Prerequisites:** Java 17+, PostgreSQL 14+, Gradle wrapper (`./gradlew`).

```bash
# Database
docker compose up -d
# Apply SQL scripts (see docs for details)
for f in sql/[0-9]*.postgres.sql; do psql -U postgres -d afcas -f "$f"; done

# Build CLI fat JAR
./gradlew shadowJar
java -jar app/build/libs/afcas.jar -h localhost -p 5432 -U postgres -W
```

Native image (GraalVM):

```bash
./gradlew nativeCompile
./app/build/native/nativeCompile/afcas
```

Docker (native image Dockerfile):

```bash
docker build -t afcas-app .
docker run --name afcas-app -it afcas-app
```

### License

See [LICENSE](LICENSE).
