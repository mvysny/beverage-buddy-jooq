/*
 * This file is generated by jOOQ.
 */
package com.vaadin.starter.beveragebuddy.backend.jooq.tables.records


import com.vaadin.starter.beveragebuddy.backend.HasValidity
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Category
import jakarta.validation.constraints.NotBlank

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record2
import org.jooq.Row2
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class CategoryRecord() : UpdatableRecordImpl<CategoryRecord>(Category.CATEGORY), Record2<Long?, String?>, HasValidity {

    open var id: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    open var name: String?
        set(value): Unit = set(1, value)
        @NotBlank
        get(): String? = get(1) as String?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row2<Long?, String?> = super.fieldsRow() as Row2<Long?, String?>
    override fun valuesRow(): Row2<Long?, String?> = super.valuesRow() as Row2<Long?, String?>
    override fun field1(): Field<Long?> = Category.CATEGORY.ID
    override fun field2(): Field<String?> = Category.CATEGORY.NAME
    override fun component1(): Long? = id
    override fun component2(): String? = name
    override fun value1(): Long? = id
    override fun value2(): String? = name

    override fun value1(value: Long?): CategoryRecord {
        set(0, value)
        return this
    }

    override fun value2(value: String?): CategoryRecord {
        set(1, value)
        return this
    }

    override fun values(value1: Long?, value2: String?): CategoryRecord {
        this.value1(value1)
        this.value2(value2)
        return this
    }

    /**
     * Create a detached, initialised CategoryRecord
     */
    constructor(id: Long? = null, name: String? = null): this() {
        this.id = id
        this.name = name
        resetChangedOnNotNull()
    }
}
