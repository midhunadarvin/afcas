# Project Structure

```text
afcas/
├── app/
│   ├── build.gradle
│   ├── libs/
│   │   └── postgresql-42.6.0.jar
│   └── src/
│       ├── main/
│       │   ├── java/com/afcas/
│       │   │   ├── AfcasApp.java              # CLI entry
│       │   │   ├── CliCommands.java
│       │   │   ├── commands/                  # PicoCLI commands
│       │   │   ├── config/EngineConfiguration.java
│       │   │   ├── engine/                    # Java auth engine
│       │   │   ├── factory/
│       │   │   ├── impl/                      # Manager + SQL provider
│       │   │   ├── objects/                   # Domain model
│       │   │   ├── repository/                # Repos + jdbc/
│       │   │   ├── utils/                     # DatabaseHelper, etc.
│       │   │   └── web/                       # Spring Boot UI
│       │   └── resources/
│       │       ├── application.properties
│       │       ├── db/init/                   # Embedded SQL scripts
│       │       ├── templates/                 # Thymeleaf
│       │       └── META-INF/native-image/     # GraalVM configs
│       └── test/java/com/afcas/
├── sql/                                       # Source of truth SQL scripts
│   └── test/
├── docs/                                      # VitePress documentation (this site)
├── docker-compose.yaml
├── Dockerfile
├── gradlew
├── settings.gradle
├── README.md
├── ARCHITECTURE.md
└── BUILD_AND_RUN.md
```

## Documentation sources

Long-form markdown at the repo root was used as the basis for this VitePress site. Prefer **`docs/`** for browsing; root markdown remains for offline/git reading.
