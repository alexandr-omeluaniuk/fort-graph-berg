plugins {
    id("org.beryx.jlink") version "2.24.1"
    application
}

group = "ss.fortberg"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core", "jackson-core", "2.18.0")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.18.0")
    // testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

application {
    mainClass.set("ss.fortberg.Main")
}

jlink {
    launcher {
        name = "Fort GraphBerg"
    }
    jvmVersion = 23
    javaHome = "/home/alex/.jdks/openjdk-23.0.2"
}