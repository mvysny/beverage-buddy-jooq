package com.vaadin.starter.beveragebuddy.ui

import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.starter.beveragebuddy.backend.*
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.ui.categories.CategoriesList
import com.vaadin.starter.beveragebuddy.ui.categories.CategoryRow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.expect

/**
 * Tests the UI. Uses the Browserless Testing approach as provided by the [Karibu Testing](https://github.com/mvysny/karibu-testing) library.
 */
class CategoriesListTest : AbstractAppTest() {
    @BeforeEach fun navigate() {
        // navigate to the "Categories" list route.
        navigateTo<CategoriesList>()
    }

    @Test fun `grid lists all categories`() {
        // prepare testing data
        db { CATEGORY.dao.insert(Category(name = "Beers")) }

        // now the "Categories" list should be displayed. Look up the Grid and assert on its contents.
        val grid = _get<Grid<CategoryRecord>>()
        grid.expectRows(1)
        grid.expectRow(0, "Beers", "0", "Button[text='Edit', icon='vaadin:edit', @class='category__edit', @theme='tertiary']")
    }

    @Test fun `create new category`() {
        _get<Button> { text = "New category (Alt+N)" } ._click()

        // make sure that the "New Category" dialog is opened
        _expectOne<EditorDialogFrame<*>>()
    }

    @Test fun `edit existing category`() {
        // prepare testing data
        Category(name = "Beers").create()

        val grid = _get<Grid<CategoryRecord>>()
        grid.expectRow(0, "Beers", "0", "Button[text='Edit', icon='vaadin:edit', @class='category__edit', @theme='tertiary']")
        grid._clickRenderer(0, "edit")

        // make sure that the "Edit Category" dialog is opened
        _expectOne<EditorDialogFrame<*>>()
        expect("Beers") { _get<TextField> { label = "Category Name" } ._value }
    }

    @Test fun `edit existing category via context menu`() {
        val cat = Category(name = "Beers")
        cat.create()

        val grid = _get<Grid<CategoryRow>>()
        grid.expectRow(0, "Beers", "0", "Button[text='Edit', icon='vaadin:edit', @class='category__edit', @theme='tertiary']")
        _get<CategoriesList>().gridContextMenu._clickItemWithCaption("Edit (Alt+E)", CategoryRow(CategoryRecord(cat), 0))

        // make sure that the "Edit Category" dialog is opened
        _expectOne<EditorDialogFrame<*>>()
        expect(cat.name) { _get<TextField> { label = "Category Name" } ._value }
    }

    @Test fun `delete existing category via context menu`() {
        val cat = Category(name = "Beers")
        cat.create()

        val grid = _get<Grid<CategoryRow>>()
        grid.expectRow(0, "Beers", "0", "Button[text='Edit', icon='vaadin:edit', @class='category__edit', @theme='tertiary']")
        _get<CategoriesList>().gridContextMenu._clickItemWithCaption("Delete", CategoryRow(CategoryRecord(cat), 0))

        // check that the category has been deleted in the database.
        expectList() { db { CATEGORY.dao.findAll().toList() } }
        _get<Grid<CategoryRecord>>().expectRows(0)
        expectNotifications("Category successfully deleted.")
    }

    @Test fun `delete existing category via context menu clears the category in review`() {
        val cat = Category(name = "Beers")
        cat.create()
        val review = Review(name = "Foo", score = 1, date = LocalDate.now(), category = cat.id!!, count = 1)
        review.create()

        val grid = _get<Grid<CategoryRow>>()
        grid.expectRow(0, "Beers", "1", "Button[text='Edit', icon='vaadin:edit', @class='category__edit', @theme='tertiary']")
        _get<CategoriesList>().gridContextMenu._clickItemWithCaption("Delete", CategoryRow(CategoryRecord(cat), 0))

        // check that the category has been deleted in the database.
        expectList() { db { CATEGORY.dao.findAll().toList() } }
        _get<Grid<CategoryRecord>>().expectRows(0)
        expectNotifications("Category successfully deleted.")
        expectList(review.copy(category = null)) { db { REVIEW.dao.findAll().toList() }}
    }
}
