package com.vaadin.starter.beveragebuddy

import com.github.mvysny.kaributools.addMetaTag
import com.gitlab.mvysny.jdbiorm.JdbiOrm
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import com.vaadin.flow.theme.Theme
import com.vaadin.starter.beveragebuddy.backend.DemoData
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import eu.vaadinonkotlin.VaadinOnKotlin
import jakarta.servlet.ServletContextEvent
import jakarta.servlet.ServletContextListener
import jakarta.servlet.annotation.WebListener
import org.flywaydb.core.Flyway
import org.h2.Driver
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
import org.slf4j.LoggerFactory

/**
 * Boots the app:
 *
 * * Makes sure that the database is up-to-date, by running migration scripts with Flyway. This will work even in cluster as Flyway
 *   automatically obtains a cluster-wide database lock.
 * * Initializes the VaadinOnKotlin framework.
 * * Maps Vaadin to `/`, maps REST server to `/rest`
 * @author mvy
 */
@WebListener
class Bootstrap: ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent?) {
        log.info("Starting up")

        // this will configure your database. For demo purposes, an in-memory embedded H2 database is used. To use a production-ready database:
        // 1. fill in the proper JDBC URL here
        // 2. make sure to include the database driver into the classpath, by adding a dependency on the driver into the build.gradle file.
        val cfg = HikariConfig().apply {
            driverClassName = Driver::class.java.name
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
            username = "sa"
            password = ""
        }
        JdbiOrm.setDataSource(HikariDataSource(cfg))

        // Initializes the VoK framework
        log.info("Initializing VaadinOnKotlin")
        VaadinOnKotlin.init()

        // Makes sure the database is up-to-date
        log.info("Running DB migrations")
        val flyway: Flyway = Flyway.configure()
            .dataSource(JdbiOrm.getDataSource())
            .load()
        flyway.migrate()

        // generate JOOQ files
        val configuration: Configuration = Configuration()
            .withJdbc(
                Jdbc()
                    .withDriver(Driver::class.java.name)
                    .withUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
                    .withUser("sa")
                    .withPassword("")
            )
            .withGenerator(
                Generator()
                    .withName("org.jooq.codegen.KotlinGenerator")
                    .withDatabase(
                        Database()
                            .withName("org.jooq.meta.h2.H2Database")
                            .withIncludes(".*")
                            .withInputSchema("PUBLIC")
                    )
                    .withTarget(
                        Target()
                            .withPackageName("com.vaadin.starter.beveragebuddy.backend.jooq")
                            .withDirectory("src/main/kotlin")
                    )
            )
        GenerationTool.generate(configuration)

        // pre-populates the database with a demo data
        log.info("Populating database with testing data")
        DemoData.createDemoData()
        log.info("Initialization complete")
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        log.info("Shutting down");
        log.info("Destroying VaadinOnKotlin")
        VaadinOnKotlin.destroy()
        log.info("Shutdown complete")
    }

    companion object {
        private val log = LoggerFactory.getLogger(Bootstrap::class.java)
    }
}

/**
 * Configures Vaadin. Registered via the Java Service Loader API.
 */
class MyServiceInitListener : VaadinServiceInitListener {
    override fun serviceInit(event: ServiceInitEvent) {
        event.addIndexHtmlRequestListener {
            it.document.head().addMetaTag("apple-mobile-web-app-capable", "yes")
            it.document.head().addMetaTag("apple-mobile-web-app-status-bar-style", "black")
        }
    }
}

@BodySize(width = "100vw", height = "100vh")
@Theme("my-theme")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
class AppShell: AppShellConfigurator
