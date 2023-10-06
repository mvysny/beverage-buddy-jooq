package com.vaadin.starter.beveragebuddy.backend.jooq.tables.records

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.vaadin.starter.beveragebuddy.backend.dao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.backend.simplejooq.isValid
import com.vaadin.starter.beveragebuddy.backend.simplejooq.single
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

    test("create()") {
        val cat = Category(name = "Foo")
        cat.create()
        expectList(cat) { db { CATEGORY.dao.findAll().toList() } }
    }

    group("delete") {
        test("smoke") {
            val cat = Category(name = "Foo")
            cat.create()
            cat.delete()
            expectList() { db { CATEGORY.dao.findAll().toList() } }
        }
        test("deleting category fixes foreign keys") {
            val cat = Category(name = "Foo")
            cat.create()
            val review = Review(name = "Foo", score = 1, date = LocalDate.now(), category = cat.id!!, count = 1)
            review.create()

            cat.delete()
            expectList() { db { CATEGORY.dao.findAll().toList() } }
            expect(null) { db { REVIEW.dao.single().category } }
        }
    }
})
