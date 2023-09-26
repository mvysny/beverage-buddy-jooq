/*
 * This file is generated by jOOQ.
 */
package com.vaadin.starter.beveragebuddy.backend.jooq.tables.records


import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Review

import java.time.LocalDate

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record6
import org.jooq.Row6
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class ReviewRecord() : UpdatableRecordImpl<ReviewRecord>(Review.REVIEW), Record6<Long?, String?, Byte?, LocalDate?, Long?, Byte?> {

    open var id: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    open var name: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    open var score: Byte?
        set(value): Unit = set(2, value)
        get(): Byte? = get(2) as Byte?

    open var date: LocalDate?
        set(value): Unit = set(3, value)
        get(): LocalDate? = get(3) as LocalDate?

    open var category: Long?
        set(value): Unit = set(4, value)
        get(): Long? = get(4) as Long?

    open var count: Byte?
        set(value): Unit = set(5, value)
        get(): Byte? = get(5) as Byte?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row6<Long?, String?, Byte?, LocalDate?, Long?, Byte?> = super.fieldsRow() as Row6<Long?, String?, Byte?, LocalDate?, Long?, Byte?>
    override fun valuesRow(): Row6<Long?, String?, Byte?, LocalDate?, Long?, Byte?> = super.valuesRow() as Row6<Long?, String?, Byte?, LocalDate?, Long?, Byte?>
    override fun field1(): Field<Long?> = Review.REVIEW.ID
    override fun field2(): Field<String?> = Review.REVIEW.NAME
    override fun field3(): Field<Byte?> = Review.REVIEW.SCORE
    override fun field4(): Field<LocalDate?> = Review.REVIEW.DATE
    override fun field5(): Field<Long?> = Review.REVIEW.CATEGORY
    override fun field6(): Field<Byte?> = Review.REVIEW.COUNT
    override fun component1(): Long? = id
    override fun component2(): String? = name
    override fun component3(): Byte? = score
    override fun component4(): LocalDate? = date
    override fun component5(): Long? = category
    override fun component6(): Byte? = count
    override fun value1(): Long? = id
    override fun value2(): String? = name
    override fun value3(): Byte? = score
    override fun value4(): LocalDate? = date
    override fun value5(): Long? = category
    override fun value6(): Byte? = count

    override fun value1(value: Long?): ReviewRecord {
        set(0, value)
        return this
    }

    override fun value2(value: String?): ReviewRecord {
        set(1, value)
        return this
    }

    override fun value3(value: Byte?): ReviewRecord {
        set(2, value)
        return this
    }

    override fun value4(value: LocalDate?): ReviewRecord {
        set(3, value)
        return this
    }

    override fun value5(value: Long?): ReviewRecord {
        set(4, value)
        return this
    }

    override fun value6(value: Byte?): ReviewRecord {
        set(5, value)
        return this
    }

    override fun values(value1: Long?, value2: String?, value3: Byte?, value4: LocalDate?, value5: Long?, value6: Byte?): ReviewRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        this.value6(value6)
        return this
    }

    /**
     * Create a detached, initialised ReviewRecord
     */
    constructor(id: Long? = null, name: String? = null, score: Byte? = null, date: LocalDate? = null, category: Long? = null, count: Byte? = null): this() {
        this.id = id
        this.name = name
        this.score = score
        this.date = date
        this.category = category
        this.count = count
        resetChangedOnNotNull()
    }
}
