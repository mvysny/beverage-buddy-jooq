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
package com.vaadin.starter.beveragebuddy.ui.reviews

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.karibudsl.v23.virtualList
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.virtuallist.VirtualList
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.starter.beveragebuddy.backend.dao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.backend.simplejooq.getById
import com.vaadin.starter.beveragebuddy.ui.MainLayout
import com.vaadin.starter.beveragebuddy.ui.Toolbar
import com.vaadin.starter.beveragebuddy.ui.getSize
import com.vaadin.starter.beveragebuddy.ui.toolbarView

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 */
@Route(value = "", layout = MainLayout::class)
@PageTitle("Review List")
class ReviewsList : KComposite() {

    private lateinit var toolbar: Toolbar
    private lateinit var header: H3
    private lateinit var reviewsGrid: VirtualList<ReviewRow>
    private val editDialog = ReviewEditorDialog { dp.refreshAll() }
    private val dp = ReviewRowDataProvider()

    private val root = ui {
        verticalLayout(false) {
            content { align(stretch, top) }
            toolbar = toolbarView("New review") {
                onSearch = { updateList() }
                onCreate = { editDialog.createNew() }
            }
            header = h3 {
                setId("header")
            }
            reviewsGrid = virtualList(dp) {
                addClassName("reviews")
                setRenderer(ComponentRenderer<ReviewItem, ReviewRow> { review ->
                    val item = ReviewItem(review)
                    item.onEdit = { db { editDialog.edit(REVIEW.dao.getById(review.review.id!!)) } }
                    item
                })
            }
        }
    }

    init {
        updateList()
    }

    private fun updateList() {
        dp.setFilter(toolbar.searchText)
        val size: Int = dp.getSize()
        if (toolbar.searchText.isBlank()) {
            header.text = "Reviews"
            header.add(Span("$size in total"))
        } else {
            header.text = "Search for “${toolbar.searchText}”"
            header.add(Span("$size results"))
        }
    }
}

/**
 * Shows a single row stripe with information about a single [ReviewWithCategory].
 */
class ReviewItem(val row: ReviewRow) : KComposite() {
    /**
     * Fired when this item is to be edited (the "Edit" button is pressed by the User).
     */
    var onEdit: () -> Unit = {}

    private val root = ui {
        div("review") {
            div("review__rating") {
                p(row.review.score.toString()) {
                    className = "review__score"
                    element.setAttribute("data-score", row.review.score.toString())
                }
                p(row.review.count.toString()) {
                    className = "review__count"
                    span("times tasted")
                }
            }
            div("review__details") {
                h4(row.review.name?:"") {
                    addClassName("review__name")
                }
                p {
                    className = "review__category"
                    if (row.review.category != null) {
                        element.themeList.add("badge small")
                        element.style.set("--category", row.review.category.toString())
                        text = row.categoryName
                    } else {
                        element.style.set("--category", "-1")
                        text = "Undefined"
                    }
                }
            }
            div("review__date") {
                h5("Last tasted")
                p(row.review.date.toString())
            }
            button("Edit") {
                icon = VaadinIcon.EDIT.create()
                className = "review__edit"
                addThemeVariants(ButtonVariant.LUMO_TERTIARY)
                onClick { onEdit() }
            }
        }
    }

    override fun toString(): String = "ReviewItem($row)"
}
