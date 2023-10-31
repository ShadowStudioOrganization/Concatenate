plugins {
    kotlin("jvm") version "1.8.20" apply false
    id("org.jetbrains.compose") version "1.4.1" apply false
}

allprojects {
    group = "org.shadow.studio"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
}



