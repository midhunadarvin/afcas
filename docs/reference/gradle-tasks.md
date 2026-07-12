# Gradle Tasks Reference

Run from repository root with `./gradlew <task>`.

| Task | Description |
|------|-------------|
| `build` | Compile, test, package |
| `clean` | Remove build outputs |
| `compileJava` | Compile main sources |
| `test` | Run unit tests |
| `jar` | Plain JAR |
| `shadowJar` | Fat CLI JAR `app/build/libs/afcas.jar` |
| `bootJar` | Spring Boot executable JAR (if plugin present) |
| `bootRun` | Run web app in dev mode |
| `nativeCompile` | GraalVM native binary |
| `run` | Application plugin run (CLI main) |

## Examples

```bash
./gradlew shadowJar
./gradlew test
./gradlew nativeCompile
./gradlew clean build
```
