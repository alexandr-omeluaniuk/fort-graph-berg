plugins {
    kotlin("jvm") version "2.1.0"
    application
}

group = "ss.fortberg"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.resourcepool", "ssdp-client", "2.5.1")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.18.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}

application {
    mainClass.set("ss.fortberg.MainKt")
}