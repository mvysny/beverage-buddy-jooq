package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.starter.beveragebuddy.backend.dao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.simplejooq.attach
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.ui.categories.CategoryEditorDialog
import kotlin.test.expect

/**
 * Tests the UI. Uses the Browserless Testing approach as provided by the [Karibu Testing](https://github.com/mvysny/karibu-testing) library.
 */
class CategoryEditorDialogTest : DynaTest({

    usingApp()

    test("create new category") {
        CategoryEditorDialog {} .createNew()

        // make sure that the "New Category" dialog is opened
        _expectOne<EditorDialogFrame<*>>()

        // do the happy flow: fill in the form with valid values and click "Save"
        _get<TextField> { label = "Category Name" } .value = "Beer"
        _get<Button> { text = "Create" } ._click()
        expectNotifications("Category successfully added.")

        _expectNone<EditorDialogFrame<*>>()     // expect the dialog to close
        expect("Beer") { CATEGORY.dao.single().name }
    }

    test("edit existing category") {
        val cat = CategoryRecord(name = "Foo")
        db { cat.attach().insert() }

        CategoryEditorDialog {} .edit(cat)

        // make sure that the "New Category" dialog is opened
        _expectOne<EditorDialogFrame<*>>()

        // do the happy flow: fill in the form with valid values and click "Save"
        _get<TextField> { label = "Category Name" } .value = "Beer"
        _get<Button> { text = "Save" } ._click()
        expectNotifications("Category successfully saved.")

        _expectNone<EditorDialogFrame<*>>()     // expect the dialog to close
        expect("Beer") { CATEGORY.dao.single().name }
    }
})
