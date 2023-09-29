package com.vaadin.starter.beveragebuddy.ui.categories

import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider
import com.vaadin.flow.data.provider.Query
import com.vaadin.starter.beveragebuddy.backend.db2
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.ui.AbstractJooqDataProvider
import com.vaadin.starter.beveragebuddy.ui.fetchOneInt
import org.jooq.Condition
import org.jooq.impl.DSL
import java.io.Serializable
import java.util.stream.Stream

class CategoryDataProvider :
    AbstractJooqDataProvider<CategoryRow, Void>(CATEGORY), ConfigurableFilterDataProvider<CategoryRow, Void, String> {

    private fun getWhereClause(): Condition {
        val f = (filter?:"").trim()
        return if (f.isEmpty()) {
            DSL.trueCondition()
        } else {
            CATEGORY.NAME.likeIgnoreCase("$f%")
        }
    }

    override fun fetchFromBackEnd(query: Query<CategoryRow, Void>): Stream<CategoryRow> {
        val result: List<CategoryRow> = db2 {
            create.select(CATEGORY.asterisk(), reviewCountField)
                .from(CATEGORY)
                .where(getWhereClause())
                .orderByQuery(query)
                .limit(query.limit)
                .offset(query.offset)
                .map { CategoryRow(it.into(CATEGORY), it[reviewCountField] ?: 0) }
        }
        return result.stream()
    }

    override fun sizeInBackEnd(query: Query<CategoryRow, Void>): Int = db2 {
        create.selectCount()
            .from(CATEGORY)
            .where(getWhereClause())
            .fetchOneInt()
    }

    companion object {
        private val reviewCountField =
            DSL.field("select sum(r.count) from Review r where r.category = category.id")
                .cast(Int::class.java)
    }

    private var filter: String? = null

    override fun setFilter(filter: String?) {
        this.filter = filter
        refreshAll()
    }
}

data class CategoryRow(
    val category: CategoryRecord,
    val reviewCount: Int
) : Serializable
