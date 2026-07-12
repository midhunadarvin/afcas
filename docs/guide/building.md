# Building

AFCAS uses **Gradle** multi-project layout with root project `AFCAS` and subproject `app`.

The build supports **two entry points**:

| Entry | Main class | Primary tasks |
|-------|------------|---------------|
| CLI | `com.afcas.AfcasApp` | `shadowJar`, `:app:run` |
| Web UI | `com.afcas.web.AfcasWebApplication` | `bootJar`, `bootRun` |

## Quick builds

```bash
# CLI fat JAR
./gradlew shadowJar

# Web executable JAR
./gradlew bootJar

# Both (assemble also builds shadowJar)
./gradlew bootJar shadowJar
```

## Artifacts

| Artifact | Command | Output |
|----------|---------|--------|
| **CLI fat JAR** | `./gradlew shadowJar` | `app/build/libs/afcas.jar` |
| **Web Boot JAR** | `./gradlew bootJar` | `app/build/libs/afcas-web-1.0-SNAPSHOT.jar` |
| **Plain JAR** | `./gradlew :app:jar` | `app/build/libs/app-1.0-SNAPSHOT-plain.jar` |
| **Native image (CLI)** | `./gradlew nativeCompile` | `app/build/native/nativeCompile/afcas` |

## Run

```bash
# Web UI (dev)
./gradlew bootRun

# Web UI (packaged)
java -jar app/build/libs/afcas-web-1.0-SNAPSHOT.jar

# CLI
./gradlew shadowJar
java -jar app/build/libs/afcas.jar -h localhost -p 5432 -U postgres -W

# CLI without JAR
./gradlew :app:run --args='-h localhost -p 5432 -U postgres'
```

## Plugins

| Plugin | Role |
|--------|------|
| `org.springframework.boot` | Web packaging, `bootRun`, `bootJar` |
| `io.spring.dependency-management` | BOM-managed Spring versions |
| `java` / `application` | Compile + CLI `run` |
| `com.github.johnrengelman.shadow` | Fat CLI JAR `afcas.jar` |
| `org.graalvm.buildtools.native` | Native CLI image |

## Java toolchain

```groovy
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
```

## Dependencies (high level)

- Spring Boot starters: web, thymeleaf, jdbc, validation
- PostgreSQL JDBC
- PicoCLI + JLine2 (CLI shell)
- Guava, Lombok
- JUnit 4/5 (vintage) + Mockito for tests

## Native image / AOT note

Spring AOT processing is **disabled** in this project so dual entry points (CLI + web) do not conflict. `nativeCompile` still targets the **CLI** main class via `graalvmNative`.

## Reflection / GraalVM

When adding reflective libraries for the native CLI binary, update configs under `META-INF/native-image/`. See [Native Image](/development/native-image).
