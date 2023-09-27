package com.vaadin.starter.beveragebuddy.backend.jooq.tables.records

import com.github.mvysny.dynatest.DynaTest
import com.vaadin.starter.beveragebuddy.backend.isValid
import kotlin.test.expect

class CategoryRecordTest : DynaTest({
    group("validation") {
        test("smoke") {
            expect(false) { CategoryRecord().isValid }
            expect(false) { CategoryRecord(name = "  ").isValid }
            expect(true) { CategoryRecord(name = "F").isValid }
        }
    }
})
