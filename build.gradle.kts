import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// The Beverage Buddy sample project ported to Kotlin.
// Original project: https://github.com/vaadin/beverage-starter-flow

buildscript {
    // fix for https://github.com/mvysny/vaadin-boot-example-gradle/issues/3
    dependencies {
        classpath("com.vaadin:vaadin-prod-bundle:${project.properties["vaadinVersion"]}")
    }
}

plugins {
    kotlin("jvm") version "1.9.10"
    id("application")
    id("com.vaadin")
}

defaultTasks("clean", "build")

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        // to see the exceptions of failed tests in Travis-CI console.
        exceptionFormat = TestExceptionFormat.FULL
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // Vaadin
    implementation("com.vaadin:vaadin-core:${properties["vaadinVersion"]}") {
        afterEvaluate {
            if (vaadin.productionMode) {
                exclude(module = "vaadin-dev")
            }
        }
    }
    implementation("com.github.mvysny.karibudsl:karibu-dsl-v23:2.1.0")
    implementation("com.github.mvysny.vaadin-boot:vaadin-boot:12.1")

    implementation("com.zaxxer:HikariCP:5.0.1")

    // logging
    // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
    implementation("org.slf4j:slf4j-simple:2.0.7")

    // db
    implementation("org.flywaydb:flyway-core:9.22.1")
    implementation("com.h2database:h2:2.2.224") // remove this and replace it with a database driver of your choice.
    implementation("org.jooq:jooq:${properties["jooqVersion"]}")
    // uncomment to enable JooqGenerator
//    implementation("org.jooq:jooq-meta:${properties["jooqVersion"]}")
//    implementation("org.jooq:jooq-codegen:${properties["jooqVersion"]}")

    // REST
    api("io.javalin:javalin:${properties["javalin_version"]}") {
        exclude(group = "org.eclipse.jetty")
        exclude(group = "org.eclipse.jetty.websocket")
        exclude(group = "com.fasterxml.jackson.core")
    }
    // workaround for https://github.com/google/gson/issues/1059
    implementation("com.fatboyindustrial.gson-javatime-serialisers:gson-javatime-serialisers:1.1.1")

    // testing
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v24:2.1.0")
    testImplementation("com.github.mvysny.dynatest:dynatest:0.24")
    testImplementation("eu.vaadinonkotlin:vok-rest-client:${properties["vokVersion"]}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("com.vaadin.starter.beveragebuddy.MainKt")
}
