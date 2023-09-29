package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10._click
import com.github.mvysny.kaributesting.v10._expectNone
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.github.mvysny.kaributesting.v23.expectRows
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.virtuallist.VirtualList
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.ReviewRecord
import com.vaadin.starter.beveragebuddy.backend.simplejooq.attach
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.ui.reviews.ReviewRow
import java.time.LocalDate

class ReviewsListTest : DynaTest({

    usingApp()

    test("no reviews initially") {
        _get<VirtualList<ReviewRow>>().expectRows(0)
    }

    test("reviews listed") {
        // prepare testing data
        val cat = CategoryRecord(name = "Beers")
        db { cat.attach().store() }
        db { ReviewRecord(score = 1, name = "Good!", category = cat.id, date = LocalDate.now(), count = 1).attach().store() }
        _get<VirtualList<ReviewRow>>().expectRows(1)
    }

    test("'new review' smoke test") {
        UI.getCurrent().navigate("")
        _get<Button> { text = "New review (Alt+N)" }._click()

        // the dialog should have been opened
        _expectOne<EditorDialogFrame<*>>()

        // this is just a smoke test, so let's close the dialog
        _get<Button> { text = "Cancel" }._click()

        _expectNone<EditorDialogFrame<*>>()
    }
})
