package com.vaadin.starter.beveragebuddy.ui

import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.Result
import com.vaadin.flow.data.binder.ValueContext
import com.vaadin.flow.data.converter.Converter
import com.vaadin.flow.data.provider.*
import com.vaadin.flow.data.provider.Query
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import org.jooq.*
import org.jooq.SortOrder
import org.jooq.impl.DSL
import java.util.stream.Stream

/**
 * Provides instances of given record [R]. Accepts filter of type [Condition].
 */
class JooqRecordDataProvider<R : Record>(private val table: Table<R>) :
    AbstractJooqDataProvider<R, Condition>(table),
    ConfigurableFilterDataProvider<R, Condition, Condition> {

    private fun getWhereClause(query: Query<R, Condition>): Condition {
        var c = filter
        if (query.filter.isPresent) {
            c = c.and(query.filter.get())
        }
        return c
    }

    override fun fetchFromBackEnd(query: Query<R, Condition>): Stream<R> {
        val result = db {
            create.selectFrom(table).where(getWhereClause(query))
                .orderByQuery(query).limit(query.limit).offset(query.offset)
                .fetch()
        }
        return result.stream()
    }

    override fun sizeInBackEnd(query: Query<R, Condition>): Int = db {
        create.selectCount().from(table).where(getWhereClause(query))
            .fetchOneInt()
    }

    private var filter: Condition = DSL.trueCondition()

    override fun setFilter(filter: Condition?) {
        this.filter = filter ?: DSL.trueCondition()
        refreshAll()
    }
}

/**
 * Converts JOOQ [SortField] into Vaadin's [QuerySortOrder].
 */
val SortField<*>.querySortOrder: QuerySortOrder
    get() = QuerySortOrder(
        this@querySortOrder.`$field`().qualifiedName.toString(),
        if (order == SortOrder.DESC) SortDirection.DESCENDING else SortDirection.ASCENDING
    )

/**
 * Sets the default sorting for [JooqRecordDataProvider]. Returns this.
 */
fun <R : Record, D : JooqRecordDataProvider<R>> D.setSortFields(vararg fields: SortField<*>): D {
    setSortOrders(fields.map { it.querySortOrder })
    return this
}

/**
 * Allows [JooqRecordDataProvider] to be set to a Vaadin [com.vaadin.flow.component.combobox.ComboBox]. When the user types in something in
 * hopes to filter the items in the dropdown, [filterConverter] is invoked, to convert the user input
 * into JOOQ [Condition].
 * @param filterConverter only invoked when the user types in something. The String is guaranteed to be
 * non-null, non-blank and trimmed.
 */
fun <R : Record> DataProvider<R, Condition>.withStringFilter(filterConverter: (String) -> Condition?): DataProvider<R, String> =
    withConvertedFilter { filter: String? ->
        val postProcessedFilter = (filter ?: "").trim()
        if (postProcessedFilter.isNotEmpty()) {
            filterConverter(postProcessedFilter)
        } else {
            null
        }
    }

/**
 * Returns Vaadin [DataProvider] which provides all records from this table.
 */
val <R : Record> Table<R>.dataProvider: JooqRecordDataProvider<R>
    get() = JooqRecordDataProvider(this)

/**
 * Converts a JOOQ [TableRecord] to its ID and back. Handy when binding ComboBox
 * populated with JOOQ records to a bean property which holds the ID of that record.
 */
class RecordToIdConverter<R : TableRecord<R>, ID>(
    val idField: TableField<R, ID>
) : Converter<R?, ID?> {
    override fun convertToModel(
        value: R?, context: ValueContext
    ): Result<ID?> = Result.ok(value?.get(idField))

    override fun convertToPresentation(
        value: ID?, context: ValueContext?
    ): R? {
        if (value == null) return null
        return db {
            create.selectFrom(idField.table).where(idField.eq(value)).fetchOne()
        }
    }
}

fun <T, R : TableRecord<R>, ID> Binder.BindingBuilder<T, R?>.toId(idField: TableField<R, ID>): Binder.BindingBuilder<T, ID?> =
    withConverter(RecordToIdConverter(idField))
