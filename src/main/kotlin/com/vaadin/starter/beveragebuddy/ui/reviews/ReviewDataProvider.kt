package com.vaadin.starter.beveragebuddy.ui.reviews

import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider
import com.vaadin.flow.data.provider.Query
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.ReviewRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.REVIEW
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db2
import com.vaadin.starter.beveragebuddy.ui.AbstractJooqDataProvider
import com.vaadin.starter.beveragebuddy.ui.fetchOneInt
import org.jooq.Condition
import org.jooq.impl.DSL
import java.io.Serializable
import java.util.stream.Stream

data class ReviewRow(
    val review: ReviewRecord,
    val categoryName: String?
) : Serializable

class ReviewDataProvider : AbstractJooqDataProvider<ReviewRow, Void>(REVIEW),
    ConfigurableFilterDataProvider<ReviewRow, Void, String> {

    private var filter: String = ""

    private fun getWhereClause(): Condition = if (filter.isEmpty()) {
        DSL.trueCondition()
    } else {
        CATEGORY.NAME.likeIgnoreCase("$filter%")
            .or(REVIEW.SCORE.cast(String::class.java).likeIgnoreCase("$filter%"))
            .or(REVIEW.COUNT.cast(String::class.java).likeIgnoreCase("$filter%"))
            .or(REVIEW.NAME.cast(String::class.java).likeIgnoreCase("$filter%"))
    }

    override fun fetchFromBackEnd(query: Query<ReviewRow, Void>): Stream<ReviewRow> {
        val result = db2 {
            create.select(REVIEW.asterisk(), CATEGORY.NAME)
                .from(REVIEW)
                .leftJoin(CATEGORY).on(REVIEW.CATEGORY.eq(CATEGORY.ID))
                .where(getWhereClause())
                .orderByQuery(query)
                .offset(query.offset)
                .limit(query.limit)
                .map { ReviewRow(it.into(REVIEW), it[CATEGORY.NAME]) }
        }
        return result.stream()
    }

    override fun sizeInBackEnd(query: Query<ReviewRow, Void>): Int = db2 {
        create.selectCount()
            .from(REVIEW)
            .leftJoin(CATEGORY).on(REVIEW.CATEGORY.eq(CATEGORY.ID))
            .where(getWhereClause())
            .fetchOneInt()
    }

    override fun setFilter(filter: String?) {
        val newFilter = (filter ?: "").trim()
        if (newFilter != this.filter) {
            this.filter = newFilter
            refreshAll()
        }
    }
}
