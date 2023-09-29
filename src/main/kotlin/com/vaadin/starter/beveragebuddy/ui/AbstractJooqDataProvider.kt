package com.vaadin.starter.beveragebuddy.ui

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
import org.jooq.*
import org.jooq.impl.DSL
import java.util.*

/**
 * Helps with data loading using JOOQ to a JOOQ Record [T]. Returns a list of [T]. Extend this class and implement
 * both [AbstractBackEndDataProvider.fetchFromBackEnd] and [AbstractBackEndDataProvider.sizeInBackEnd] methods.
 * Use [DSLContext] to create SQL selects via DSL.
 *
 * To be able to determine the JOOQ sorting field properly, you need to:
 * * Set JOOQ [Field.getQualifiedName] name String to the Grid Column via [setSortField]
 * * Call [toSortField] to convert the field name back to [SortField] accepted by JOOQ. Even better, use [orderByQuery] to help with sorting.
 *
 * See extending classes for examples on how to use this class.
 * @property table the JOOQ table definition.
 * @property T the JOOQ record type to be returned. Usually a JOOQ [Record], but feel free to use a simple Kotlin data class
 * when an outcome of a JOIN of two or more tables needs to be represented. See `StoragePlaceDataProvider` for an example on this.
 * @property F the filter accepted by the backend calls; may be a String or a full-blown filter bean. The best practice is
 * to convert [Query.filter] to JOOQ [Condition], then pass it to the JOOQ query.
 */
abstract class AbstractJooqDataProvider<T, F>(private val table: Table<*>) : AbstractBackEndDataProvider<T, F>() {

    /**
     * Additional fields that may appear in [QuerySortOrder.sorted]. By default, all [table] fields are considered by
     * [toSortField], but there may be additional columns from additional joined tables; such columns should be added here otherwise
     * sorting by such Grid columns will do nothing.
     *
     * Defaults to an empty set.
     */
    protected open val additionalSortFields: Set<Field<*>> get() = setOf()

    /**
     * Looks up a JOOQ table [Field] by its name (stored in [QuerySortOrder.sorted]) and returns appropriate [SortField].
     *
     * If you have a complex Grid with columns from additional joined tables, make sure to fill in [additionalSortFields].
     */
    private fun QuerySortOrder.toSortField(): SortField<*> {
        val sortFields = table.fields() + additionalSortFields
        val field: Field<*>? = sortFields.firstOrNull { it.qualifiedName.toString() == sorted }
        requireNotNull(field) { "${sorted}: unknown field. Available fields: ${sortFields}" }
        return when {
            direction == SortDirection.ASCENDING -> field.asc()
            else -> field.desc()
        }
    }

    /**
     * Converts Vaadin [Query.sortOrders] to JOOQ [SortField] which you can pass into your JOOQ DSL
     * [SelectOrderByStep.orderByQuery] function.
     * @param defaultSortField used if no sorting is required. Defaults to sorting by primary key.
     */
    private fun Query<T, F>.getSortField(defaultSortField: SortField<*> = table.primaryKey!!.fields[0].asc()): SortField<*> {
        val sortOrder: QuerySortOrder? = sortOrders?.firstOrNull()
        return sortOrder?.toSortField() ?: defaultSortField
    }

    /**
     * Applies ordering from [query]. Call this function as any other JOOQ DSL function.
     * @param defaultSortField used if no sorting is required. E.g. `NEWSITEM.ID.asc()`. Defaults to sorting by primary key.
     */
    protected fun <R : Record> SelectOrderByStep<R>.orderByQuery(
        query: Query<T, F>,
        defaultSortField: SortField<*> = table.primaryKey!!.fields[0].asc(),
    ): SelectLimitStep<R> =
        orderBy(query.getSortField(defaultSortField))

    /**
     * Counts distinct records. Use when [fetchFromBackEnd] uses DISTINCT.
     */
    fun DSLContext.selectCountDistinct(): SelectSelectStep<Record1<Int>> =
        select(DSL.countDistinct(*table.primaryKey!!.fieldsArray))
}

fun ResultQuery<Record1<Int>>.fetchOneInt(): Int = fetchOne()?.value1() ?: 0

fun Grid.Column<*>.setSortField(sortField: Field<*>) {
    setSortProperty(sortField.qualifiedName.toString())
}
