/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.starter.beveragebuddy.ui.categories

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.ModifierKey.*
import com.github.mvysny.kaributools.addShortcut
import com.vaadin.flow.component.Key.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.starter.beveragebuddy.backend.dao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.backend.simplejooq.getById
import com.vaadin.starter.beveragebuddy.ui.*

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 */
@Route(value = "categories", layout = MainLayout::class)
@PageTitle("Categories List")
class CategoriesList : KComposite() {

    private lateinit var header: H3
    private lateinit var toolbar: Toolbar
    private lateinit var grid: Grid<CategoryRow>
    // can't retrieve GridContextMenu from Grid: https://github.com/vaadin/vaadin-grid-flow/issues/523
    lateinit var gridContextMenu: GridContextMenu<CategoryRow>

    private val editorDialog = CategoryEditorDialog { dp.refreshAll() }
    private val dp = CategoryRowDataProvider()

    private val root = ui {
        verticalLayout(false) {
            content { align(stretch, top) }
            toolbar = toolbarView("New category") {
                onSearch = { updateView() }
                onCreate = { editorDialog.createNew() }
            }
            header = h3()
            grid = grid(dataProvider = dp) {
                isExpand = true
                column({ it.category.name }) {
                    setSortField(CATEGORY.NAME)
                    setHeader("Category")
                }
                addColumn { it.reviewCount }.setHeader("Beverages")
                addColumn(ComponentRenderer<Button, CategoryRow>({ cat -> createEditButton(cat) })).apply {
                    flexGrow = 0; key = "edit"
                }
                element.themeList.add("row-dividers")

                gridContextMenu = gridContextMenu {
                    item("New", { _ -> editorDialog.createNew() })
                    item("Edit (Alt+E)", { cat -> if (cat != null) edit(cat) })
                    item("Delete", { cat -> if (cat != null) deleteCategory(cat) })
                }
            }

            addShortcut(Alt + KEY_E) {
                val row = grid.asSingleSelect().value
                if (row != null) {
                    edit(row)
                }
            }
        }
    }

    init {
        updateView()
    }

    private fun createEditButton(row: CategoryRow): Button =
        Button("Edit").apply {
            icon = Icon(VaadinIcon.EDIT)
            addClassName("category__edit")
            addThemeVariants(ButtonVariant.LUMO_TERTIARY)
            onClick { edit(row) }
        }

    private fun edit(row: CategoryRow) {
        editorDialog.edit(db { CATEGORY.dao.getById(row.category.id!!) })
    }

    private fun updateView() {
        dp.setFilter(toolbar.searchText)
        if (toolbar.searchText.isNotBlank()) {
            header.text = "Search for “${toolbar.searchText}”"
        } else {
            header.text = "Categories"
        }
    }

    private fun deleteCategory(row: CategoryRow) {
        db { CATEGORY.dao.deleteById(row.category.id!!) }
        Notification.show("Category successfully deleted.", 3000, Notification.Position.BOTTOM_START)
        updateView()
    }
}
