plugins {
    id("java-library")
    id("maven-publish")
    id("com.exactpro.th2.gradle.publish") version "0.0.5"
}

group = "com.exactpro.th2"
version = project.properties["release_version"].toString()

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    resolutionStrategy.cacheDynamicVersionsFor(0, "seconds")
}

dependencies {
    implementation("com.exactpro.th2:common:5.10.0-dev")
    implementation("com.exactpro.th2:grpc-common:4.5.0-dev")
    implementation("com.exactpro.th2:grpc-hand:3.0.0-dev")
    implementation("com.exactpro.th2:hand:5.0.2-dev")
    implementation("org.apache.commons:commons-lang3")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.slf4j:slf4j-api")
    implementation("org.apache.commons:commons-collections4")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

repositories {
    mavenCentral()
    maven {
        name = "Sonatype_snapshots"
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        name = "Sonatype_releases"
        url = uri("https://s01.oss.sonatype.org/content/repositories/releases/")
    }
}