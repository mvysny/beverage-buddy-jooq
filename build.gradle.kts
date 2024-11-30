import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// The Beverage Buddy sample project ported to Kotlin.
// Original project: https://github.com/vaadin/beverage-starter-flow

plugins {
    kotlin("jvm") version "2.1.0"
    application
    alias(libs.plugins.vaadin)
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        // to see the exception stacktraces of failed tests in the CI console
        exceptionFormat = TestExceptionFormat.FULL
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // Vaadin
    implementation(libs.vaadin.core) {
        if (vaadin.effective.productionMode.get()) {
            exclude(module = "vaadin-dev")
        }
    }
    implementation(libs.karibu.dsl)
    implementation(libs.vaadin.boot)

    implementation(libs.hikaricp)

    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation(libs.slf4j.simple)

    // db
    implementation(libs.flyway)
    implementation(libs.h2) // remove this and replace it with a database driver of your choice.
    implementation(libs.jooq.jooq)
    // uncomment to enable JooqGenerator
//    implementation(libs.jooq.meta)
//    implementation(libs.jooq.codegen)

    // REST
    implementation(libs.http4k)
    // workaround for https://github.com/google/gson/issues/1059
    implementation(libs.gson.javatime)

    // testing
    testImplementation(libs.karibu.testing)
    testImplementation(libs.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<KotlinCompile> {
    compilerOptions.jvmTarget = JvmTarget.JVM_21 // we use virtual threads: see PipedStreamUtil for more details.
}

java {
    sourceCompatibility = JavaVersion.VERSION_21 // we use virtual threads: see PipedStreamUtil for more details.
    targetCompatibility = JavaVersion.VERSION_21
    // commented out: otherwise the build fails on github actions with jdk 22
//    toolchain {
//        languageVersion = JavaLanguageVersion.of(21) // we use virtual threads: see PipedStreamUtil for more details.
//    }
}

application {
    mainClass = "com.vaadin.starter.beveragebuddy.MainKt"
}
