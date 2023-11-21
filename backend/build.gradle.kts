
plugins {
    application
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-client-okhttp-jvm:2.3.5")
    implementation("io.ktor:ktor-client-core:2.3.5")
    implementation("io.ktor:ktor-client-cio:2.3.5")

    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")

    implementation("org.apache.maven.resolver:maven-resolver-api:1.9.16")
    implementation("org.apache.maven:maven-resolver-provider:3.9.5")
    implementation("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.16")
    implementation("org.apache.maven.resolver:maven-resolver-transport-http:1.9.16")
    implementation("org.apache.maven.resolver:maven-resolver-impl:1.9.16")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")


}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("org.shadow.studio.concatenate.backend.MainKt")
}