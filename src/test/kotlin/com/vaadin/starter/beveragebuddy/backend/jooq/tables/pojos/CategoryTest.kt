package com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos

import com.github.mvysny.kaributesting.v10.expectList
import com.vaadin.starter.beveragebuddy.backend.dao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.backend.simplejooq.single
import com.vaadin.starter.beveragebuddy.ui.AbstractAppTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.expect

class CategoryTest : AbstractAppTest() {
    @Nested inner class Validation {
        @Test fun smoke() {
            expect(false) { Category().isValid }
            expect(false) { Category(name = "  ").isValid }
            expect(true) { Category(name = "F").isValid }
        }
    }

    @Nested inner class Create {
        @Test fun simple() {
            val cat = Category(name = "Foo")
            cat.create()
            expectList(cat) { db { CATEGORY.dao.findAll().toList() } }
        }
        @Test fun `create() works with pre-assigned IDs`() {
            val cat = Category(id = 1L, name = "Foo")
            cat.create()
            expect(1L) { cat.id }
            expectList(cat) { db { CATEGORY.dao.findAll().toList() } }
        }
    }

    @Nested inner class Delete {
        @Test fun smoke() {
            val cat = Category(name = "Foo")
            cat.create()
            cat.delete()
            expectList() { db { CATEGORY.dao.findAll().toList() } }
        }
        @Test fun `deleting category fixes foreign keys`() {
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
}
