plugins {
    id("org.beryx.jlink") version "3.1.1"
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
        name = "FortGraphBerg"
    }
    jvmVersion = 23
    javaHome = "C:\\MinecraftServer\\jdk-23.0.2"
    jpackage {
        imageOptions.apply {
            add("--win-console")
            icon = "src/main/resources/icons/carrot-solid-full.ico"
        }
    }
}