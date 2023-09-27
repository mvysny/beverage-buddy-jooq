package com.vaadin.starter.beveragebuddy

/*
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
*/

/**
 * By default it does nothing. To use this generator:
 *
 * 1. Uncomment the `jooq-meta` and `jooq-codegen` in `build.gradle.kts` and load Gradle changes in IDEA
 * 2. Uncomment the code blocks in this file.
 * 3. Run the app. The code will be generated on app start.
 */
object JooqGenerator {
    fun generate() {
/*
        val configuration: Configuration = Configuration()
            .withJdbc(
                Jdbc()
                    .withDriver("org.h2.Driver")
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
                    ).withGenerate(
                        Generate().withValidationAnnotations(true)
                    ).withTarget(
                        Target()
                            .withPackageName("com.vaadin.starter.beveragebuddy.backend.jooq")
                            .withDirectory("src/main/kotlin")
                    )
            )
        GenerationTool.generate(configuration)
*/
    }
}