#FROM gradle:7.4-jdk11
#RUN mkdir /project
#COPY . /project
#WORKDIR /project
#RUN gradle nativeCompile
#CMD ["app/build/native/nativeCompile/afcas"]

# Use a base image with GraalVM and Gradle pre-installed
FROM ghcr.io/graalvm/graalvm-community:latest
# Set the working directory
WORKDIR /project

# Copy the Gradle build files
# COPY build.gradle .
# COPY settings.gradle .
# COPY gradlew .
# COPY gradle gradle

# Copy the source code
COPY . .

# Run Gradle build
RUN ./gradlew nativeCompile --no-daemon

RUN ls
# Copy the built JAR file to a separate directory
RUN mkdir /project/build-output

RUN ls

RUN cp app/build/native/nativeCompile/afcas /project/build-output/

EXPOSE 5432

# Set the entry point to run the Java program using GraalVM
CMD ["/project/build-output/afcas"]