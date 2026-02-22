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
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.18.0")
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
}