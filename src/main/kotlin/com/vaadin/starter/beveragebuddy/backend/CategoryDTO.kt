package com.vaadin.starter.beveragebuddy.backend

import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import java.io.Serializable

class CategoryDTO(
    val id: Long,
    val name: String
) : Serializable

fun CategoryRecord.dto() = CategoryDTO(id!!, name!!)
fun List<CategoryRecord>.dto() = map { it.dto() }
