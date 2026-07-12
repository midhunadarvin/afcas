# Native Image (GraalVM)

## Build

```bash
# Requires GraalVM with native-image
gu install native-image   # if needed

./gradlew nativeCompile
# Binary: app/build/native/nativeCompile/afcas
./app/build/native/nativeCompile/afcas
```

## Gradle config

`app/build.gradle` uses:

```groovy
id 'org.graalvm.buildtools.native' version '0.9.23'

graalvmNative {
    toolchainDetection = false
    metadataRepository { enabled = true }
    binaries {
        main { imageName.set('afcas') }
    }
}
```

## Reflection metadata

Configs live under:

```text
app/src/main/resources/META-INF/native-image/
  jni-config.json
  predefined-classes-config.json
  proxy-config.json
  reflect-config.json
  resource-config.json
  serialization-config.json
```

When adding libraries that use reflection, regenerate or extend `reflect-config.json`. GraalVM agent support:

https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html#agent-support

## Docker native build

See repository `Dockerfile` (GraalVM community image + `nativeCompile`).
