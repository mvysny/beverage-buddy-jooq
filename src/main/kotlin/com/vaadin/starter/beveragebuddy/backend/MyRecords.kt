package com.vaadin.starter.beveragebuddy.backend

import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Review
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.daos.ReviewDao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.Dao
import com.vaadin.starter.beveragebuddy.backend.simplejooq.currentConfiguration
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
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
object CategoryDao : Dao<CategoryRecord, Long>(CATEGORY) {
    override fun deleteAll() {
        db {
            create.update(REVIEW).setNull(REVIEW.CATEGORY).execute()
            super.deleteAll()
        }
    }

    fun findByName(name: String): CategoryRecord? =
        db { create.fetchOne(CATEGORY, CATEGORY.NAME.eq(name)) }

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
val Category.dao: CategoryDao get() = CategoryDao
