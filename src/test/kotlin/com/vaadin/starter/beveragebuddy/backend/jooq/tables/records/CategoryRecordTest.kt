package com.vaadin.starter.beveragebuddy.backend.jooq.tables.records

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.vaadin.starter.beveragebuddy.backend.dao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.attach
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.backend.simplejooq.isValid
import com.vaadin.starter.beveragebuddy.ui.usingApp
import java.time.LocalDate
import kotlin.test.expect

class CategoryRecordTest : DynaTest({
    usingApp()

    group("validation") {
        test("smoke") {
            expect(false) { CategoryRecord().isValid }
            expect(false) { CategoryRecord(name = "  ").isValid }
            expect(true) { CategoryRecord(name = "F").isValid }
        }
    }

    group("delete") {
        test("smoke") {
            val cat = CategoryRecord(name = "Foo")
            db { cat.attach().store() }
            CATEGORY.dao.delete(cat)
            expectList() { CATEGORY.dao.findAll().toList() }
        }
        test("deleting category fixes foreign keys") {
            val cat = CategoryRecord(name = "Foo")
            db { cat.attach().store() }
            val review = ReviewRecord(name = "Foo", score = 1, date = LocalDate.now(), category = cat.id!!, count = 1)
            db { review.attach().store() }

            CATEGORY.dao.delete(cat)
            expectList() { CATEGORY.dao.findAll().toList() }
            expect(null) { REVIEW.dao.single().category }
        }
    }
})
