plugins {
    kotlin("jvm") version "1.8.21"
    application
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("SpaceInvaders")
}

javafx {
    version = "18.0.2"
    modules("javafx.controls", "javafx.graphics", "javafx.media")
}


