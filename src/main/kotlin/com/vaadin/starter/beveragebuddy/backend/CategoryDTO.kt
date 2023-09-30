package com.vaadin.starter.beveragebuddy.backend

import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import java.io.Serializable

/**
 * Used from [JavalinRestServlet] - we can't export JOOQ Records directly since
 * they contain countless private fields which would be exported to JSON.
 */
class CategoryDTO(
    val id: Long,
    val name: String
) : Serializable

fun CategoryRecord.dto() = CategoryDTO(id!!, name!!)
fun List<CategoryRecord>.dto() = map { it.dto() }
