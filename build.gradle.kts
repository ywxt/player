plugins {
    kotlin("jvm") version "1.4.10"
}

group = "ywxt.pi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("uk.co.caprica:vlcj:4.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0-M1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.4.0-M1")
}
