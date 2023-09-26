/*
 * This file is generated by jOOQ.
 */
package com.vaadin.starter.beveragebuddy.backend.jooq.tables


import com.vaadin.starter.beveragebuddy.backend.jooq.Public
import com.vaadin.starter.beveragebuddy.backend.jooq.indexes.FLYWAY_SCHEMA_HISTORY_S_IDX
import com.vaadin.starter.beveragebuddy.backend.jooq.keys.FLYWAY_SCHEMA_HISTORY_PK
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.FlywaySchemaHistoryRecord

import java.time.LocalDateTime
import java.util.function.Function

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Index
import org.jooq.Name
import org.jooq.Record
import org.jooq.Records
import org.jooq.Row10
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
open class FlywaySchemaHistory(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, FlywaySchemaHistoryRecord>?,
    aliased: Table<FlywaySchemaHistoryRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<FlywaySchemaHistoryRecord>(
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
         * The reference instance of <code>PUBLIC.flyway_schema_history</code>
         */
        val FLYWAY_SCHEMA_HISTORY: FlywaySchemaHistory = FlywaySchemaHistory()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<FlywaySchemaHistoryRecord> = FlywaySchemaHistoryRecord::class.java

    /**
     * The column <code>PUBLIC.flyway_schema_history.installed_rank</code>.
     */
    val INSTALLED_RANK: TableField<FlywaySchemaHistoryRecord, Int?> = createField(DSL.name("installed_rank"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>PUBLIC.flyway_schema_history.version</code>.
     */
    val VERSION: TableField<FlywaySchemaHistoryRecord, String?> = createField(DSL.name("version"), SQLDataType.VARCHAR(50), this, "")

    /**
     * The column <code>PUBLIC.flyway_schema_history.description</code>.
     */
    val DESCRIPTION: TableField<FlywaySchemaHistoryRecord, String?> = createField(DSL.name("description"), SQLDataType.VARCHAR(200).nullable(false), this, "")

    /**
     * The column <code>PUBLIC.flyway_schema_history.type</code>.
     */
    val TYPE: TableField<FlywaySchemaHistoryRecord, String?> = createField(DSL.name("type"), SQLDataType.VARCHAR(20).nullable(false), this, "")

    /**
     * The column <code>PUBLIC.flyway_schema_history.script</code>.
     */
    val SCRIPT: TableField<FlywaySchemaHistoryRecord, String?> = createField(DSL.name("script"), SQLDataType.VARCHAR(1000).nullable(false), this, "")

    /**
     * The column <code>PUBLIC.flyway_schema_history.checksum</code>.
     */
    val CHECKSUM: TableField<FlywaySchemaHistoryRecord, Int?> = createField(DSL.name("checksum"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>PUBLIC.flyway_schema_history.installed_by</code>.
     */
    val INSTALLED_BY: TableField<FlywaySchemaHistoryRecord, String?> = createField(DSL.name("installed_by"), SQLDataType.VARCHAR(100).nullable(false), this, "")

    /**
     * The column <code>PUBLIC.flyway_schema_history.installed_on</code>.
     */
    val INSTALLED_ON: TableField<FlywaySchemaHistoryRecord, LocalDateTime?> = createField(DSL.name("installed_on"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)), this, "")

    /**
     * The column <code>PUBLIC.flyway_schema_history.execution_time</code>.
     */
    val EXECUTION_TIME: TableField<FlywaySchemaHistoryRecord, Int?> = createField(DSL.name("execution_time"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>PUBLIC.flyway_schema_history.success</code>.
     */
    val SUCCESS: TableField<FlywaySchemaHistoryRecord, Boolean?> = createField(DSL.name("success"), SQLDataType.BOOLEAN.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<FlywaySchemaHistoryRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<FlywaySchemaHistoryRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>PUBLIC.flyway_schema_history</code> table
     * reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>PUBLIC.flyway_schema_history</code> table
     * reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>PUBLIC.flyway_schema_history</code> table reference
     */
    constructor(): this(DSL.name("flyway_schema_history"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, FlywaySchemaHistoryRecord>): this(Internal.createPathAlias(child, key), child, key, FLYWAY_SCHEMA_HISTORY, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIndexes(): List<Index> = listOf(FLYWAY_SCHEMA_HISTORY_S_IDX)
    override fun getPrimaryKey(): UniqueKey<FlywaySchemaHistoryRecord> = FLYWAY_SCHEMA_HISTORY_PK
    override fun `as`(alias: String): FlywaySchemaHistory = FlywaySchemaHistory(DSL.name(alias), this)
    override fun `as`(alias: Name): FlywaySchemaHistory = FlywaySchemaHistory(alias, this)
    override fun `as`(alias: Table<*>): FlywaySchemaHistory = FlywaySchemaHistory(alias.getQualifiedName(), this)

    /**
     * Rename this table
     */
    override fun rename(name: String): FlywaySchemaHistory = FlywaySchemaHistory(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): FlywaySchemaHistory = FlywaySchemaHistory(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): FlywaySchemaHistory = FlywaySchemaHistory(name.getQualifiedName(), null)

    // -------------------------------------------------------------------------
    // Row10 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row10<Int?, String?, String?, String?, String?, Int?, String?, LocalDateTime?, Int?, Boolean?> = super.fieldsRow() as Row10<Int?, String?, String?, String?, String?, Int?, String?, LocalDateTime?, Int?, Boolean?>

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    fun <U> mapping(from: (Int?, String?, String?, String?, String?, Int?, String?, LocalDateTime?, Int?, Boolean?) -> U): SelectField<U> = convertFrom(Records.mapping(from))

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    fun <U> mapping(toType: Class<U>, from: (Int?, String?, String?, String?, String?, Int?, String?, LocalDateTime?, Int?, Boolean?) -> U): SelectField<U> = convertFrom(toType, Records.mapping(from))
}