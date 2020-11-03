# TH2-Act template

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

This project uses environment variables as its settings

ENV VAR NAME | DEFAULT VALUE | DESCRIPTION
------------ | ------------- | -----------
TH2_EVENT_STORAGE_GRPC_HOST | localhost | Event storage gRPC host
TH2_EVENT_STORAGE_GRPC_PORT | 8080 | Event storage gRPC port
HAND_GRPC_HOST | localhost | TH2-Hand gRPC host
HAND_GRPC_PORT | 8080 | TH2-Hand gRPC port
GRPC_PORT | 8090 | TH2-Act gRPC Server port to run on
SESSION_EXPIRATION | 30 | Session expiration time
