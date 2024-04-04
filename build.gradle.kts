plugins {
    id("java-library")
    id("maven-publish")

    val th2PluginVersion = "0.0.4"
    id("com.exactpro.th2.gradle.publish") version th2PluginVersion
}

allprojects {
    val version = project.properties["release_version"].toString()
    val suffix = project.properties["version_suffix"]?.toString() ?: ""
    this.group = "com.exactpro.th2"
    this.version = version + if (suffix.isEmpty()) "" else "-$suffix"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    resolutionStrategy.cacheDynamicVersionsFor(0, "seconds")
}

dependencies {
    implementation("com.exactpro.th2:common:5.10.0-dev")
    implementation("com.exactpro.th2:grpc-common:4.4.0-dev")
    implementation("com.exactpro.th2:grpc-hand:3.0.0-RM-84612-8544324408-SNAPSHOT")
    implementation("com.exactpro.th2:hand:5.0.0-RM-84612-no-log4j-8545363969-62a26ef-SNAPSHOT")
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