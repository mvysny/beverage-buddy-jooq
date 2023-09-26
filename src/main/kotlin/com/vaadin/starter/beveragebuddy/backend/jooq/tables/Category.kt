/*
 * This file is generated by jOOQ.
 */
package com.vaadin.starter.beveragebuddy.backend.jooq.tables


import com.vaadin.starter.beveragebuddy.backend.jooq.Public
import com.vaadin.starter.beveragebuddy.backend.jooq.indexes.IDX_CATEGORY_NAME
import com.vaadin.starter.beveragebuddy.backend.jooq.keys.CONSTRAINT_3
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord

import java.util.function.Function

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Index
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row2
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
open class Category(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, CategoryRecord>?,
    aliased: Table<CategoryRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<CategoryRecord>(
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
         * The reference instance of <code>PUBLIC.CATEGORY</code>
         */
        val CATEGORY: Category = Category()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<CategoryRecord> = CategoryRecord::class.java

    /**
     * The column <code>PUBLIC.CATEGORY.ID</code>.
     */
    val ID: TableField<CategoryRecord, Long?> = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false).identity(true), this, "")

    /**
     * The column <code>PUBLIC.CATEGORY.NAME</code>.
     */
    val NAME: TableField<CategoryRecord, String?> = createField(DSL.name("NAME"), SQLDataType.VARCHAR(200).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<CategoryRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<CategoryRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>PUBLIC.CATEGORY</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>PUBLIC.CATEGORY</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>PUBLIC.CATEGORY</code> table reference
     */
    constructor(): this(DSL.name("CATEGORY"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, CategoryRecord>): this(Internal.createPathAlias(child, key), child, key, CATEGORY, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIndexes(): List<Index> = listOf(IDX_CATEGORY_NAME)
    override fun getIdentity(): Identity<CategoryRecord, Long?> = super.getIdentity() as Identity<CategoryRecord, Long?>
    override fun getPrimaryKey(): UniqueKey<CategoryRecord> = CONSTRAINT_3
    override fun `as`(alias: String): Category = Category(DSL.name(alias), this)
    override fun `as`(alias: Name): Category = Category(alias, this)
    override fun `as`(alias: Table<*>): Category = Category(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Category = Category(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Category = Category(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Category = Category(name.getQualifiedName(), null)

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row2<Long?, String?> = super.fieldsRow() as Row2<Long?, String?>

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    fun <U> mapping(from: (Long?, String?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (Long?, String?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}
