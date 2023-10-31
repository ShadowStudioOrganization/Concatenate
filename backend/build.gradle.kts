
plugins {
    application
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-client-okhttp-jvm:2.3.5")
    implementation("io.ktor:ktor-client-core:2.3.5")
    implementation("io.ktor:ktor-client-cio:2.3.5")
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