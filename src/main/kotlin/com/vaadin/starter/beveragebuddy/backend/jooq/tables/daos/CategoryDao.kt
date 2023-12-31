/*
 * This file is generated by jOOQ.
 */
package com.vaadin.starter.beveragebuddy.backend.jooq.tables.daos


import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.impl.DAOImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class CategoryDao(configuration: Configuration?) : DAOImpl<CategoryRecord, com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category, Long>(Category.CATEGORY, com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category::class.java, configuration) {

    /**
     * Create a new CategoryDao without any configuration
     */
    constructor(): this(null)

    override fun getId(o: com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category): Long? = o.id

    /**
     * Fetch records that have <code>ID BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfId(lowerInclusive: Long?, upperInclusive: Long?): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category> = fetchRange(Category.CATEGORY.ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>ID IN (values)</code>
     */
    fun fetchById(vararg values: Long): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category> = fetch(Category.CATEGORY.ID, *values.toTypedArray())

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    fun fetchOneById(value: Long): com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category? = fetchOne(Category.CATEGORY.ID, value)

    /**
     * Fetch records that have <code>NAME BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfName(lowerInclusive: String?, upperInclusive: String?): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category> = fetchRange(Category.CATEGORY.NAME, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>NAME IN (values)</code>
     */
    fun fetchByName(vararg values: String): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category> = fetch(Category.CATEGORY.NAME, *values)
}
