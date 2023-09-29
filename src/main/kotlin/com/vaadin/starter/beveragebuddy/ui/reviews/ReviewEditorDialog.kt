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
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.data.binder.Binder
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.ReviewRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.simplejooq.attach
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db2
import com.vaadin.starter.beveragebuddy.ui.*
import java.time.LocalDate

/**
 * A dialog for editing [Review] objects.
 */
class ReviewEditorForm : FormLayout(), EditorForm<ReviewRecord> {
    override val itemType: String get() = "Review"
    // to propagate the changes made in the fields by the user, we will use binder to bind the field to the Review property.
    override val binder: Binder<ReviewRecord> = beanValidationBinder()

    init {
        responsiveSteps {
            "0"(1); "50em"(2)
        }

        textField("Beverage name") {
            // no need to have validators here: they are automatically picked up from the bean field.
            bind(binder).trimmingConverter().bind(ReviewRecord::name)
        }
        integerField("Times tasted") {
            bind(binder)
                .toByte()
                .bind(ReviewRecord::count)
        }
        comboBox<CategoryRecord>("Choose a category") {
            // we need to show a list of options for the user to choose from. For every option we need to retain at least:
            // 1. the category ID (to bind it to Review::category)
            // 2. the category name (to show it to the user when the combobox's option list is expanded)
            // since the Category class already provides these values, we will simply use that as the data source for the options.
            //
            // now we need to configure the item label generator so that we can extract the name out of Category and display it to the user:
            setItemLabelGenerator { it.name }

            // can't create new Categories here
            isAllowCustomValue = false

            // provide the list of options as a DataProvider, providing instances of Category
            setItems(
                CATEGORY.dataProvider
                    .setSortFields(CATEGORY.NAME.asc())
                    .withStringFilter { CATEGORY.NAME.likeIgnoreCase("$it%") }
            )

            // bind the combo box to the Review::category field so that changes done by the user are stored.
            bind(binder).toId(CATEGORY.ID).bind(ReviewRecord::category)
        }
        datePicker("Choose the date") {
            max = LocalDate.now()
            min = LocalDate.of(1, 1, 1)
            bind(binder).bind(ReviewRecord::date)
        }
        comboBox<Int>("Mark a score") {
            isAllowCustomValue = false
            setItems((1..5).toList())
            bind(binder).toByte().bind(ReviewRecord::score)
        }
    }
}

/**
 * A dialog for editing [ReviewRecord] objects.
 * @property onReviewsChanged Callback when an item has been created/saved/deleted
 */
class ReviewEditorDialog(private val onReviewsChanged: (ReviewRecord) -> Unit) {
    fun createNew() {
        edit(ReviewRecord(date = LocalDate.now()))
    }

    private fun maybeDelete(frame: EditorDialogFrame<ReviewRecord>, item: ReviewRecord) {
        ConfirmationDialog().open(
            "Delete beverage",
            "Are you sure you want to delete beverage '${item.name}'?",
            "",
            "Delete",
            true
        ) {
            db2 { item.attach().delete() }
            Notification.show("Beverage successfully deleted.", 3000, Notification.Position.BOTTOM_START)
            frame.close()
            onReviewsChanged(item)
        }
    }

    fun edit(review: ReviewRecord) {
        val frame: EditorDialogFrame<ReviewRecord> = EditorDialogFrame(ReviewEditorForm())
        frame.onSaveItem = {
            val creating: Boolean = review.id == null
            db2 { review.attach().store() }
            val op = if (creating) "added" else "saved"
            Notification.show("Beverage successfully ${op}.", 3000, Notification.Position.BOTTOM_START)
            onReviewsChanged(review)
        }
        frame.onDeleteItem = { item -> maybeDelete(frame, item) }
        frame.open(review, review.id == null)
    }
}
