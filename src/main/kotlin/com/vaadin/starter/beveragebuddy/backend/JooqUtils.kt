package com.vaadin.starter.beveragebuddy.backend

import com.github.vokorm.db
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import org.jdbi.v3.core.Handle
import org.jooq.*
import org.jooq.impl.DSL
import java.sql.Connection

/**
 * Makes sure given block is executed in a DB transaction. When the block finishes normally, the transaction commits;
 * if the block throws any exception, the transaction is rolled back.
 *
 * Example of use: `db2 { create.query("") }`
 * @param block the block to run in the transaction. Builder-style provides helpful methods and values, e.g. [JooqContext.handle]
 */
public fun <R> db2(block: JooqContext.() -> R): R = db {
    val ctx = JooqContext(handle, DSL.using(jdbcConnection, SQLDialect.H2))
    ctx.block()
}

public class JooqContext(
    public val handle: Handle,
    public val create: DSLContext
) {
    /**
     * The underlying JDBC connection.
     */
    public val jdbcConnection: Connection get() = handle.connection
}

/**
 * See [DSLContext.executeInsert].
 */
fun TableRecord<*>.executeInsert(): Int = db2 { create.executeInsert(this@executeInsert) }

@Suppress("UNCHECKED_CAST")
fun <R : Record> Table<R>.getById(id: Long): R = db2 {
    val idField = javaClass.getDeclaredField("ID")
        .get(this@getById) as TableField<R, Long?>
    create.fetchSingle(this@getById, idField.eq(id))
}

fun Category.getByName(name: String): CategoryRecord =
    db2 { create.fetchSingle(this@getByName, NAME.eq(name)) }
