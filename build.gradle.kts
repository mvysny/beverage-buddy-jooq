import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// The Beverage Buddy sample project ported to Kotlin.
// Original project: https://github.com/vaadin/beverage-starter-flow

plugins {
    kotlin("jvm") version "2.3.0"
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
    // Vaadin
    implementation(libs.vaadin.core)
    if (!vaadin.effective.productionMode.get()) {
        implementation(libs.vaadin.dev)
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
    implementation(libs.bundles.http4k)
    implementation(libs.bundles.gson)

    // testing
    testImplementation(libs.karibu.testing)
    testImplementation(libs.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<KotlinCompile> {
    // we use virtual threads: see PipedStreamUtil for more details.
    // also Vaadin 25 requires JVM 21+
    compilerOptions.jvmTarget = JvmTarget.JVM_21
}

java {
    sourceCompatibility = JavaVersion.VERSION_21 // we use virtual threads: see PipedStreamUtil for more details.
    targetCompatibility = JavaVersion.VERSION_21
}

application {
    mainClass = "com.vaadin.starter.beveragebuddy.MainKt"
}
