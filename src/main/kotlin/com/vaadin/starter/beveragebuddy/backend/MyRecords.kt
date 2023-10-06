package com.vaadin.starter.beveragebuddy.backend

import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Review
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.daos.CategoryDao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.daos.ReviewDao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.currentConfiguration
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.backend.simplejooq.deleteAll
import org.jooq.impl.DSL

class ReviewDaoExt : ReviewDao(currentConfiguration()) {
    fun getTotalCountForReviewsInCategory(categoryId: Long): Int = db {
        val result = create.select(DSL.sum(REVIEW.COUNT))
            .from(REVIEW)
            .where(REVIEW.CATEGORY.eq(categoryId))
            .fetchOne()?.value1()
        result?.toInt() ?: 0
    }
}

val Review.dao: ReviewDaoExt get() = ReviewDaoExt()

/**
 * The [CATEGORY] DAO with useful finder methods.
 */
class CategoryDaoExt : CategoryDao(currentConfiguration()) {
    fun deleteAllAndClearReviewFKs() {
        db {
            create.update(REVIEW).setNull(REVIEW.CATEGORY).execute()
            deleteAll()
        }
    }

    fun findByName(name: String): com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category? {
        val cats = fetchByName(name)
        check(cats.size <= 1)
        return cats.firstOrNull()
    }

    fun getByName(name: String): CategoryRecord =
        db { create.fetchSingle(CATEGORY, CATEGORY.NAME.eq(name)) }

    fun existsWithName(name: String): Boolean = findByName(name) != null

    override fun deleteById(id: Long) = db {
        create.update(REVIEW).setNull(REVIEW.CATEGORY).where(REVIEW.CATEGORY.eq(id)).execute()
        super.deleteById(id)
    }
}

/**
 * Enables you to write `CATEGORY.dao.findByName("Foo")`.
 */
val Category.dao: CategoryDaoExt get() = CategoryDaoExt()
