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

fun <R : Record> Table<R>.getById(id: Long): R = db {
    create.fetchSingle(this@getById, idField.eq(id))
}

fun <R : Record> Table<R>.single(): R = db { create.fetchSingle(this@single) }

fun <R : Record> Table<R>.deleteAll(): Int =
    db { create.deleteFrom(this@deleteAll).execute() }

fun <R : Record> Table<R>.findAll(): List<R> =
    db { create.selectFrom(this@findAll).fetch() }

// @todo mavi there's no way to override the functions, e.g. `deleteAll()` function
// to detach from foreign keys etc. Maybe refactor to the Dao<R, ID> and DaoOfAny<R> classes
// which would contain overridable methods.
// Note that this would still disallow the Record instance methods overriding, e.g. delete(),
// or store() which would perform validation. Well you can't have everything :shrug:
