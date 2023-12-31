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
import org.jooq.Configuration
import org.jooq.impl.DSL

class ReviewDaoExt(cfg: Configuration? = currentConfiguration()) : ReviewDao(cfg) {
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
class CategoryDaoExt(cfg: Configuration? = currentConfiguration()) : CategoryDao(cfg) {
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

    override fun deleteById(ids: Collection<Long>) {
        db {
            clearCategoryForReviews(ids.toSet())
            super.deleteById(ids)
        }
    }

    override fun delete(objects: Collection<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category>) {
        db {
            clearCategoryForReviews(objects.map { it.id!! } .toSet())
            super.delete(objects)
        }
    }

    private fun clearCategoryForReviews(categoryIDs: Set<Long>) {
        db {
            create.update(REVIEW).setNull(REVIEW.CATEGORY).where(REVIEW.CATEGORY.`in`(categoryIDs)).execute()
        }
    }
}

/**
 * Enables you to write `CATEGORY.dao.findByName("Foo")`.
 */
val Category.dao: CategoryDaoExt get() = CategoryDaoExt()
