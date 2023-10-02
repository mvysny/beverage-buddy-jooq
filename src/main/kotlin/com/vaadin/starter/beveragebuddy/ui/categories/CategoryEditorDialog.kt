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

import com.github.mvysny.karibudsl.v10.beanValidationBinder
import com.github.mvysny.karibudsl.v10.bind
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.trimmingConverter
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.validator.StringLengthValidator
import com.vaadin.starter.beveragebuddy.backend.*
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.attach
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.ui.ConfirmationDialog
import com.vaadin.starter.beveragebuddy.ui.EditorForm
import com.vaadin.starter.beveragebuddy.ui.EditorDialogFrame

/**
 * A form for editing [Category] objects.
 */
class CategoryEditorForm(val category: CategoryRecord) : FormLayout(), EditorForm<CategoryRecord> {
    private val isEditing get() = category.id != null
    override val itemType: String get() = "Category"
    override val binder: Binder<CategoryRecord> = beanValidationBinder()
    init {
        textField("Category Name") {
            bind(binder)
                    .trimmingConverter()
                    .withValidator(StringLengthValidator(
                            "Category name must contain at least 3 printable characters",
                            3, null))
                    .withValidator({ name -> isNameUnique(name) }, "Category name must be unique")
                    .bind(CategoryRecord::name)
        }
    }

    private fun isNameUnique(name: String?): Boolean {
        if (name.isNullOrBlank()) return true
        if (category.name == name && isEditing) return true
        return !CATEGORY.dao.existsWithName(name)
    }
}

/**
 * Opens dialogs for editing [Category] objects.
 * @property onCategoriesChanged called when a category has been created/edited/deleted.
 */
class CategoryEditorDialog(private val onCategoriesChanged: (CategoryRecord) -> Unit) {
    private fun maybeDelete(frame: EditorDialogFrame<CategoryRecord>, item: CategoryRecord) {
        val reviewCount = REVIEW.dao.getTotalCountForReviewsInCategory(item.id!!)
        if (reviewCount == 0) {
            delete(frame, item)
        } else {
            ConfirmationDialog().open(
                "Delete Category “${item.name}”?",
                "There are $reviewCount reviews associated with this category.",
                "Deleting the category will mark the associated reviews as “undefined”. You may link the reviews to other categories on the edit page.",
                "Delete",
                true
            ) {
                delete(frame, item)
            }
        }
    }

    private fun delete(frame: EditorDialogFrame<CategoryRecord>, item: CategoryRecord) {
        CATEGORY.dao.delete(item)
        Notification.show("Category successfully deleted.", 3000, Notification.Position.BOTTOM_START)
        frame.close()
        onCategoriesChanged(item)
    }

    fun createNew() {
        edit(CategoryRecord())
    }

    fun edit(category: CategoryRecord) {
        val frame = EditorDialogFrame(CategoryEditorForm(category))
        frame.onSaveItem = {
            val creating: Boolean = category.id == null
            db { category.attach().store() }
            val op: String = if (creating) "added" else "saved"
            Notification.show("Category successfully ${op}.", 3000, Notification.Position.BOTTOM_START)
            onCategoriesChanged(category)
        }
        frame.onDeleteItem = { item -> maybeDelete(frame, item) }
        frame.open(category, category.id == null)
    }
}
