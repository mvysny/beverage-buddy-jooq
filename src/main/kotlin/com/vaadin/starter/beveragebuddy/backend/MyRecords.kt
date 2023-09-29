package com.vaadin.starter.beveragebuddy.backend

import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Review
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db2
import com.vaadin.starter.beveragebuddy.backend.simplejooq.deleteAll
import org.jooq.Table
import org.jooq.impl.DSL

fun Category.findByName(name: String): CategoryRecord? =
    db2 { create.fetchOne(this@findByName, NAME.eq(name)) }

fun Category.getByName(name: String): CategoryRecord =
    db2 { create.fetchSingle(this@getByName, NAME.eq(name)) }

fun Category.existsWithName(name: String): Boolean = findByName(name) != null

fun Review.getTotalCountForReviewsInCategory(categoryId: Long): Int = db2 {
    val result = create.select(DSL.sum(REVIEW.COUNT))
        .from(REVIEW)
        .where(REVIEW.CATEGORY.eq(categoryId))
        .fetchOne()?.value1()
    result?.toInt() ?: 0
}

fun Category.deleteAllCategories(): Int = db2 {
    create.update(REVIEW).setNull(REVIEW.CATEGORY).execute()
    deleteAll()
}
