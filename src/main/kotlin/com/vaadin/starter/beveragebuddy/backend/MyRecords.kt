package com.vaadin.starter.beveragebuddy.backend

import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord

fun Category.findByName(name: String): CategoryRecord? =
    db2 { create.fetchOne(this@findByName, NAME.eq(name)) }
fun Category.getByName(name: String): CategoryRecord =
    db2 { create.fetchSingle(this@getByName, NAME.eq(name)) }
fun Category.existsWithName(name: String): Boolean = findByName(name) != null
