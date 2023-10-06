/*
 * This file is generated by jOOQ.
 */
package com.vaadin.starter.beveragebuddy.backend.jooq.tables.daos


import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Review
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.ReviewRecord

import java.time.LocalDate

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.impl.DAOImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class ReviewDao(configuration: Configuration?) : DAOImpl<ReviewRecord, com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review, Long>(Review.REVIEW, com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review::class.java, configuration) {

    /**
     * Create a new ReviewDao without any configuration
     */
    constructor(): this(null)

    override fun getId(o: com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review): Long? = o.id

    /**
     * Fetch records that have <code>ID BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfId(lowerInclusive: Long?, upperInclusive: Long?): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetchRange(Review.REVIEW.ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>ID IN (values)</code>
     */
    fun fetchById(vararg values: Long): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetch(Review.REVIEW.ID, *values.toTypedArray())

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    fun fetchOneById(value: Long): com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review? = fetchOne(Review.REVIEW.ID, value)

    /**
     * Fetch records that have <code>NAME BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfName(lowerInclusive: String?, upperInclusive: String?): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetchRange(Review.REVIEW.NAME, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>NAME IN (values)</code>
     */
    fun fetchByName(vararg values: String): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetch(Review.REVIEW.NAME, *values)

    /**
     * Fetch records that have <code>SCORE BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfScore(lowerInclusive: Byte?, upperInclusive: Byte?): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetchRange(Review.REVIEW.SCORE, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>SCORE IN (values)</code>
     */
    fun fetchByScore(vararg values: Byte): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetch(Review.REVIEW.SCORE, *values.toTypedArray())

    /**
     * Fetch records that have <code>DATE BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfDate(lowerInclusive: LocalDate?, upperInclusive: LocalDate?): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetchRange(Review.REVIEW.DATE, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>DATE IN (values)</code>
     */
    fun fetchByDate(vararg values: LocalDate): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetch(Review.REVIEW.DATE, *values)

    /**
     * Fetch records that have <code>CATEGORY BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfCategory(lowerInclusive: Long?, upperInclusive: Long?): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetchRange(Review.REVIEW.CATEGORY, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>CATEGORY IN (values)</code>
     */
    fun fetchByCategory(vararg values: Long): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetch(Review.REVIEW.CATEGORY, *values.toTypedArray())

    /**
     * Fetch records that have <code>COUNT BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfCount(lowerInclusive: Byte?, upperInclusive: Byte?): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetchRange(Review.REVIEW.COUNT, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>COUNT IN (values)</code>
     */
    fun fetchByCount(vararg values: Byte): List<com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Review> = fetch(Review.REVIEW.COUNT, *values.toTypedArray())
}