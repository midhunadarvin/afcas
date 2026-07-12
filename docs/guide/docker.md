# Docker

## PostgreSQL via Compose

```bash
docker compose up -d
```

Service:

- Image: `postgres:14.4`
- Port: `5432`
- Password: `POSTGRES_PASSWORD` or default `123456`
- Named volume: `pg_data`

## Application Dockerfile

The repository `Dockerfile` builds a **GraalVM native image**:

```dockerfile
FROM ghcr.io/graalvm/graalvm-community:latest
WORKDIR /project
COPY . .
RUN ./gradlew nativeCompile --no-daemon
RUN mkdir /project/build-output
RUN cp app/build/native/nativeCompile/afcas /project/build-output/
CMD ["/project/build-output/afcas"]
```

Build and run:

```bash
docker build -t afcas-app .
docker run --name afcas-app -it afcas-app
```

Point the container at a reachable PostgreSQL host (e.g. Compose service name or `host.docker.internal` on Docker Desktop).

## JVM example

```bash
./gradlew shadowJar
docker run -it --rm \
  -e AFCAS_DATABASE_HOST=host.docker.internal \
  -p 8080:8080 \
  -v "$PWD/app/build/libs:/app" \
  eclipse-temurin:17-jre \
  java -jar /app/afcas.jar
```

(Adjust image and entrypoint to match how you package the web vs CLI artifact.)
