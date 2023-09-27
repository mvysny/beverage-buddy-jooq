package com.vaadin.starter.beveragebuddy.backend.jooq.tables.records

import com.github.mvysny.dynatest.DynaTest
import java.time.LocalDate
import kotlin.test.expect

class ReviewRecordTest : DynaTest({
    group("validation") {
        test("smoke") {
            val validRecord = ReviewRecord(name = "Foo", category = 1L, score = 3, date = LocalDate.now(), count = 5)
            expect(true) { validRecord.isValid }
            expect(false) { ReviewRecord().isValid }
            expect(false) { validRecord.copy().apply { name = null } .isValid }
            expect(false) { validRecord.copy().apply { category = null }.isValid }
            expect(false) { validRecord.copy().apply { name = "F" }.isValid }
            expect(false) { validRecord.copy().apply { score = 10 }.isValid }
        }
    }
})
