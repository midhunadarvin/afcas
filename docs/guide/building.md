# Building

AFCAS uses **Gradle** multi-project layout with root project `AFCAS` and subproject `app`.

## Quick build

```bash
./gradlew build --no-daemon
```

## Artifact types

| Artifact | Command | Output |
|----------|---------|--------|
| **Shadow (fat) JAR** | `./gradlew shadowJar` | `app/build/libs/afcas.jar` |
| **Spring Boot JAR** | `./gradlew bootJar` | `app/build/libs/app-1.0-SNAPSHOT.jar` (when plugin configured) |
| **Plain JAR** | `./gradlew jar` | `app/build/libs/` |
| **Native image** | `./gradlew nativeCompile` | `app/build/native/nativeCompile/afcas` |

### Recommended

- **CLI / general use:** `shadowJar` → `afcas.jar` with all dependencies (including bundled PostgreSQL driver).
- **Web server:** Boot JAR or `bootRun` when Spring Boot packaging is available.
- **Minimal deploy footprint:** GraalVM native image (requires GraalVM + native-image).

## Examples

```bash
./gradlew shadowJar
./gradlew nativeCompile
./gradlew clean test
```

## Java toolchain

`app/build.gradle` sets:

```groovy
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
```

## Key plugins

| Plugin | Role |
|--------|------|
| `java` / `application` | Compile and run CLI main class `com.afcas.AfcasApp` |
| `com.github.johnrengelman.shadow` | Fat JAR `afcas.jar` |
| `org.graalvm.buildtools.native` | Native image build |

## Dependencies (CLI core)

- PicoCLI + picocli-shell-jline2 — interactive shell
- Guava
- PostgreSQL JDBC (`app/libs/postgresql-42.6.0.jar`)
- Lombok (compile-only)
- JUnit 4 + Mockito (tests)

## Reflection / GraalVM note

When adding dependencies that use reflection, update GraalVM `reflect-config.json` (and related files under `META-INF/native-image/`). See [Native Image](/development/native-image).
