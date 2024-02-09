# TH2-Act template (3.0.0)

TH2-Act contains a library of functions to be called from a script. 
TH2-Act calls TH2-Hand to perform corresponding actions in GUI and verifies the results. 

### Requirements

* JDK 8+ (OpenJDK 11 is recommended)
* Gradle (Optional)
* Docker

### Build

This project is built by Gradle.
You cat use Gradle wrapper to build it:
``` shell script
./gradlew build
```


### Configuration

This project uses common factories

## Release notes:

### 3.0.0

#### Updated lib:
+ bom: `4.5.0`
+ common: `5.8.0-dev`
+ grpc-common `4.4.0-dev`
+ grpc-hand: `3.0.0-dev`

#### Added lib:
+ common-utils: `2.2.2-dev`

#### Updated plugin:
+ io.github.gradle-nexus.publish-plugin: `1.3.0`

#### Added plugin:
+ org.owasp.dependencycheck: `9.0.9`
+ com.gorylenko.gradle-git-properties: `2.4.1`
+ com.github.jk1.dependency-license-report: `2.5`
+ de.undercouch.download: `5.4.0`
