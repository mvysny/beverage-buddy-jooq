package com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.expect

class ReviewTest {
    @Nested inner class Validation {
        @Test fun smoke() {
            val validPojo = Review(name = "Foo", category = 1L, score = 3, date = LocalDate.now(), count = 5)
            expect(true) { validPojo.isValid }
            expect(false) { Review().isValid }
            expect(false) { validPojo.copy().apply { name = null } .isValid }
            expect(false) { validPojo.copy().apply { category = null }.isValid }
            expect(false) { validPojo.copy().apply { name = "F" }.isValid }
            expect(false) { validPojo.copy().apply { score = 10 }.isValid }
        }
    }
}
