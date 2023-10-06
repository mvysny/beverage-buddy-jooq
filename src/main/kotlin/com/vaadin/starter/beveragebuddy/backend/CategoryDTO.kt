package com.vaadin.starter.beveragebuddy.backend

import com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord

fun CategoryRecord.dto() = Category(id!!, name!!)
fun List<CategoryRecord>.dto() = map { it.dto() }
