package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.starter.beveragebuddy.backend.dao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.backend.simplejooq.single
import com.vaadin.starter.beveragebuddy.ui.categories.CategoryEditorDialog
import org.junit.jupiter.api.Test
import kotlin.test.expect

/**
 * Tests the UI. Uses the Browserless Testing approach as provided by the [Karibu Testing](https://github.com/mvysny/karibu-testing) library.
 */
class CategoryEditorDialogTest : AbstractAppTest() {
    @Test fun `create new category`() {
        CategoryEditorDialog {} .createNew()

        // make sure that the "New Category" dialog is opened
        _expectOne<EditorDialogFrame<*>>()

        // do the happy flow: fill in the form with valid values and click "Save"
        _get<TextField> { label = "Category Name" } .value = "Beer"
        _get<Button> { text = "Create" } ._click()
        expectNotifications("Category successfully added.")

        _expectNone<EditorDialogFrame<*>>()     // expect the dialog to close
        expect("Beer") { db { CATEGORY.dao.single().name } }
    }

    @Test fun `edit existing category`() {
        val cat = Category(name = "Foo")
        cat.create()

        CategoryEditorDialog {} .edit(cat)

        // make sure that the "New Category" dialog is opened
        _expectOne<EditorDialogFrame<*>>()

        // do the happy flow: fill in the form with valid values and click "Save"
        _get<TextField> { label = "Category Name" } .value = "Beer"
        _get<Button> { text = "Save" } ._click()
        expectNotifications("Category successfully saved.")

        _expectNone<EditorDialogFrame<*>>()     // expect the dialog to close
        expect("Beer") { db { CATEGORY.dao.single().name } }
    }
}
