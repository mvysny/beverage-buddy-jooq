package com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.vaadin.starter.beveragebuddy.backend.dao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.backend.simplejooq.single
import com.vaadin.starter.beveragebuddy.ui.usingApp
import java.time.LocalDate
import kotlin.test.expect

class CategoryTest : DynaTest({
    usingApp()

    group("validation") {
        test("smoke") {
            expect(false) { Category().isValid }
            expect(false) { Category(name = "  ").isValid }
            expect(true) { Category(name = "F").isValid }
        }
    }

    group("create()") {
        test("simple") {
            val cat = Category(name = "Foo")
            cat.create()
            expectList(cat) { db { CATEGORY.dao.findAll().toList() } }
        }
        test("create() works with pre-assigned IDs") {
            val cat = Category(id = 1L, name = "Foo")
            cat.create()
            expect(1L) { cat.id }
            expectList(cat) { db { CATEGORY.dao.findAll().toList() } }
        }
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
            val review = Review(
                name = "Foo",
                score = 1,
                date = LocalDate.now(),
                category = cat.id!!,
                count = 1
            )
            review.create()

            cat.delete()
            expectList() { db { CATEGORY.dao.findAll().toList() } }
            expect(null) { db { REVIEW.dao.single().category } }
        }
    }
})