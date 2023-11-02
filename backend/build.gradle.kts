
plugins {
    application
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-client-okhttp-jvm:2.3.5")
    implementation("io.ktor:ktor-client-core:2.3.5")
    implementation("io.ktor:ktor-client-cio:2.3.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")

    implementation("org.apache.maven.resolver:maven-resolver-api:1.9.16")
    implementation("org.apache.maven:maven-resolver-provider:3.9.5")
    implementation("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.16")
    implementation("org.apache.maven.resolver:maven-resolver-transport-http:1.9.16")
    implementation("org.apache.maven.resolver:maven-resolver-impl:1.9.16")



}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("org.shadow.studio.concatenate.backend.MainKt")
}