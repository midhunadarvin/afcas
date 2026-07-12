# Gradle Tasks Reference

Run from repository root with `./gradlew <task>` (or `./gradlew :app:<task>`).

```bash
./gradlew tasks --all
```

## Web UI (Spring Boot)

| Task | Description |
|------|-------------|
| `bootRun` | Run web UI (`com.afcas.web.AfcasWebApplication`) |
| `bootJar` | Executable web JAR → `app/build/libs/afcas-web-1.0-SNAPSHOT.jar` |

```bash
./gradlew bootRun
./gradlew bootRun --args='--afcas.database.password=secret'
./gradlew bootJar
java -jar app/build/libs/afcas-web-1.0-SNAPSHOT.jar
```

## CLI

| Task | Description |
|------|-------------|
| `:app:run` | Run CLI main class `com.afcas.AfcasApp` |
| `:app:runShadow` | Run via shadow JAR |
| `shadowJar` | Fat CLI JAR → `app/build/libs/afcas.jar` |
| `nativeCompile` | GraalVM native CLI binary |
| `nativeRun` | Run the native binary |

```bash
./gradlew :app:run --args='-h localhost -p 5432 -U postgres'
./gradlew shadowJar
java -jar app/build/libs/afcas.jar -h localhost -p 5432 -U postgres -W
```

## Build / test

| Task | Description |
|------|-------------|
| `build` | Compile, test, package |
| `clean` | Remove build outputs |
| `compileJava` | Compile main sources |
| `test` | Run unit tests |
| `jar` | Plain JAR (classifier `plain`) |

## Examples

```bash
./gradlew bootRun
./gradlew bootJar shadowJar
./gradlew test
./gradlew clean build
```
