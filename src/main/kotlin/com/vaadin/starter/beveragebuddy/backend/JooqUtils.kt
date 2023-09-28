package com.vaadin.starter.beveragebuddy.backend

import com.github.vokorm.db
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.jdbi.v3.core.Handle
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.UpdatableRecordImpl
import java.sql.Connection

/**
 * Makes sure given block is executed in a DB transaction. When the block finishes normally, the transaction commits;
 * if the block throws any exception, the transaction is rolled back.
 *
 * Example of use: `db2 { create.query("") }`
 * @param block the block to run in the transaction. Builder-style provides helpful methods and values, e.g. [JooqContext.handle]
 */
fun <R> db2(block: JooqContext.() -> R): R = db {
    val ctx = JooqContext(handle, DSL.using(jdbcConnection, SQLDialect.H2))
    ctx.block()
}

class JooqContext(
    val handle: Handle,
    val create: DSLContext
) {
    /**
     * The underlying JDBC connection.
     */
    val jdbcConnection: Connection get() = handle.connection
}

/**
 * See [DSLContext.executeInsert].
 */
fun TableRecord<*>.executeInsert(): Int = db2 { create.executeInsert(this@executeInsert) }
fun UpdatableRecordImpl<*>.executeUpdate(): Int = db2 { create.executeUpdate(this@executeUpdate) }
fun UpdatableRecordImpl<*>.executeDelete(): Int = db2 { create.executeDelete(this@executeDelete) }
fun UpdatableRecordImpl<*>.save(): Int = if (id == null) executeInsert() else executeUpdate()

@Suppress("UNCHECKED_CAST")
val <R : Record> Table<R>.idField: TableField<R, Long?>
    get() = javaClass.getDeclaredMethod("getID").invoke(this@idField) as TableField<R, Long?>
var <R: TableRecord<R>> TableRecord<R>.id: Long?
    get() = get(table.idField)
    set(value) {
        set(table.idField, value)
    }

fun <R : Record> Table<R>.getById(id: Long): R = db2 {
    create.fetchSingle(this@getById, idField.eq(id))
}

fun Category.findByName(name: String): CategoryRecord? =
    db2 { create.fetchOne(this@findByName, NAME.eq(name)) }
fun Category.getByName(name: String): CategoryRecord =
    db2 { create.fetchSingle(this@getByName, NAME.eq(name)) }
fun Category.existsWithName(name: String): Boolean = findByName(name) != null

object JooqUtils {
    @Volatile
    var validator: Validator = Validation.buildDefaultValidatorFactory().validator
}

val Record.isValid: Boolean
    get() = try {
        validate()
        true
    } catch (ex: ConstraintViolationException) {
        false
    }

fun Record.validate() {
    val violations = JooqUtils.validator.validate<Any>(this)
    if (violations.isNotEmpty()) {
        throw ConstraintViolationException(violations)
    }
}
