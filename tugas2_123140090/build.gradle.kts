plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "com.tugas2"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("SimpleNewsTickerKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

kotlin {
    jvmToolchain(17)
}
