package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.Routes
import com.vaadin.starter.beveragebuddy.Bootstrap
import com.vaadin.starter.beveragebuddy.backend.dao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.backend.simplejooq.deleteAll
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach

// since there is no servlet environment, Flow won't auto-detect the @Routes. We need to auto-discover all @Routes
// and populate the RouteRegistry properly.
private val routes = Routes().autoDiscoverViews("com.vaadin.starter.beveragebuddy")

abstract class AbstractAppTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun bootstrapApp() {
            Bootstrap().contextInitialized(null)
        }
        @AfterAll @JvmStatic fun teardownApp() { Bootstrap().contextDestroyed(null) }
    }

    @BeforeEach fun setupVaadin() { MockVaadin.setup(routes) }
    @AfterEach fun teardownVaadin() { MockVaadin.tearDown() }

    // it's a good practice to clear up the db before every test, to start every test with a predefined state.
    @BeforeEach @AfterEach
    fun cleanupDb() { db { REVIEW.dao.deleteAll(); CATEGORY.dao.deleteAll(); } }
}
