import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    application
}
group = "me.cooleck"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

val ktor_version: String by project

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-auth:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
}
//tasks.withType<KotlinCompile>() {
//    kotlinOptions.jvmTarget = "1.8"
//}
application {
    mainClassName = "client.ClientKt"
}

tasks.getByName<JavaExec>("run") {
    standardInput = System.`in`
}