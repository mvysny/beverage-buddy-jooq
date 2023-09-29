package com.vaadin.starter.beveragebuddy.backend.simplejooq

import org.jooq.Record
import org.jooq.Table
import org.jooq.TableField

@Suppress("UNCHECKED_CAST")
val <R : Record> Table<R>.idField: TableField<R, Long?>
    get() {
        val idFields = primaryKey!!.fields
        require(idFields.size == 1) { "${this@idField} has no PK or a composite one: $idFields" }
        return idFields[0] as TableField<R, Long?>
    }

fun <R : Record> Table<R>.getById(id: Long): R = db2 {
    create.fetchSingle(this@getById, idField.eq(id))
}

fun <R : Record> Table<R>.single(): R = db2 { create.fetchSingle(this@single) }

fun <R : Record> Table<R>.deleteAll(): Int =
    db2 { create.deleteFrom(this@deleteAll).execute() }

fun <R : Record> Table<R>.findAll(): List<R> =
    db2 { create.selectFrom(this@findAll).fetch() }
