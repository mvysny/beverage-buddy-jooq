/*
 * This file is generated by jOOQ.
 */
package com.vaadin.starter.beveragebuddy.backend.jooq.tables


import com.vaadin.starter.beveragebuddy.backend.jooq.Public
import com.vaadin.starter.beveragebuddy.backend.jooq.indexes.IDX_REVIEW_NAME
import com.vaadin.starter.beveragebuddy.backend.jooq.keys.CONSTRAINT_8
import com.vaadin.starter.beveragebuddy.backend.jooq.keys.CONSTRAINT_8F
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.ReviewRecord

import java.time.LocalDate
import java.util.function.Function

import kotlin.collections.List

import org.jooq.Check
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Index
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row6
import org.jooq.Schema
import org.jooq.SelectField
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Review(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, ReviewRecord>?,
    aliased: Table<ReviewRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<ReviewRecord>(
    alias,
    Public.PUBLIC,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>PUBLIC.REVIEW</code>
         */
        val REVIEW: Review = Review()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<ReviewRecord> = ReviewRecord::class.java

    /**
     * The column <code>PUBLIC.REVIEW.ID</code>.
     */
    val ID: TableField<ReviewRecord, Long?> = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false).identity(true), this, "")

    /**
     * The column <code>PUBLIC.REVIEW.NAME</code>.
     */
    val NAME: TableField<ReviewRecord, String?> = createField(DSL.name("NAME"), SQLDataType.VARCHAR(200).nullable(false), this, "")

    /**
     * The column <code>PUBLIC.REVIEW.SCORE</code>.
     */
    val SCORE: TableField<ReviewRecord, Byte?> = createField(DSL.name("SCORE"), SQLDataType.TINYINT.nullable(false), this, "")

    /**
     * The column <code>PUBLIC.REVIEW.DATE</code>.
     */
    val DATE: TableField<ReviewRecord, LocalDate?> = createField(DSL.name("DATE"), SQLDataType.LOCALDATE.nullable(false), this, "")

    /**
     * The column <code>PUBLIC.REVIEW.CATEGORY</code>.
     */
    val CATEGORY: TableField<ReviewRecord, Long?> = createField(DSL.name("CATEGORY"), SQLDataType.BIGINT, this, "")

    /**
     * The column <code>PUBLIC.REVIEW.COUNT</code>.
     */
    val COUNT: TableField<ReviewRecord, Byte?> = createField(DSL.name("COUNT"), SQLDataType.TINYINT.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<ReviewRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<ReviewRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>PUBLIC.REVIEW</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>PUBLIC.REVIEW</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>PUBLIC.REVIEW</code> table reference
     */
    constructor(): this(DSL.name("REVIEW"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, ReviewRecord>): this(Internal.createPathAlias(child, key), child, key, REVIEW, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIndexes(): List<Index> = listOf(IDX_REVIEW_NAME)
    override fun getIdentity(): Identity<ReviewRecord, Long?> = super.getIdentity() as Identity<ReviewRecord, Long?>
    override fun getPrimaryKey(): UniqueKey<ReviewRecord> = CONSTRAINT_8
    override fun getReferences(): List<ForeignKey<ReviewRecord, *>> = listOf(CONSTRAINT_8F)

    private lateinit var _category: Category

    /**
     * Get the implicit join path to the <code>PUBLIC.CATEGORY</code> table.
     */
    fun category(): Category {
        if (!this::_category.isInitialized)
            _category = Category(this, CONSTRAINT_8F)

        return _category;
    }

    val category: Category
        get(): Category = category()
    override fun getChecks(): List<Check<ReviewRecord>> = listOf(
        Internal.createCheck(this, DSL.name("R_COUNT_RANGE"), "(\"COUNT\" >= 1)\n    AND (\"COUNT\" <= 99)", true),
        Internal.createCheck(this, DSL.name("R_SCORE_RANGE"), "(\"SCORE\" >= 1)\n    AND (\"SCORE\" <= 5)", true)
    )
    override fun `as`(alias: String): Review = Review(DSL.name(alias), this)
    override fun `as`(alias: Name): Review = Review(alias, this)
    override fun `as`(alias: Table<*>): Review = Review(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Review = Review(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Review = Review(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Review = Review(name.getQualifiedName(), null)

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row6<Long?, String?, Byte?, LocalDate?, Long?, Byte?> = super.fieldsRow() as Row6<Long?, String?, Byte?, LocalDate?, Long?, Byte?>

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    fun <U> mapping(from: (Long?, String?, Byte?, LocalDate?, Long?, Byte?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (Long?, String?, Byte?, LocalDate?, Long?, Byte?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}