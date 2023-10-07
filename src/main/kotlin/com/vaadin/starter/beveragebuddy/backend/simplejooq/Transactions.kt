package com.vaadin.starter.beveragebuddy.backend.simplejooq

import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.UpdatableRecord
import org.jooq.impl.DSL
import java.sql.Connection
import java.sql.SQLException

/**
 * Holds reference to the current JDBC connection when running in the `db{}` block.
 *
 * Replace with ScopedValue when targeting Java 21+
 */
private val jooqContextThreadLocal = ThreadLocal<JooqContextInt>()

/**
 * Sets given [value] to this [ThreadLocal], then clears it when the block terminates (either successfully,
 * or exceptionally).
 */
private fun <T, R> ThreadLocal<T>.scoped(value: T, block: () -> R): R {
    set(value)
    try {
        return block()
    } finally {
        set(null)
    }
}

/**
 * Makes sure given block is executed in a DB transaction. When the block finishes normally, the transaction commits;
 * if the block throws any exception, the transaction is rolled back.
 *
 * Example of use: `db { create.query("yada yada") }`
 * @param block the block to run in the transaction.
 */
fun <R> db(block: JooqContext.() -> R): R {
    val currentContext: JooqContextInt? = jooqContextThreadLocal.get()
    if (currentContext != null) {
        // already in a transaction - nested call to db{}. Simply run the block right away.
        return currentContext.ctx.block()
    }

    // no transaction. Obtain the JDBC connection and create the context.
    return SimpleJooq.dataSource.connection.use { jdbcConnection ->
        // create the JooqContextInt
        val ctx = JooqContextInt.create(jdbcConnection)
        // set the JooqContextInt to the thread-local and run the block in transaction
        jooqContextThreadLocal.scoped(ctx) {
            ctx.runInTransaction { ctx.ctx.block() }
        }
    }
}

/**
 * @property create JDBI [DSLContext].
 * @property jdbcConnection the underlying JDBC connection.
 */
class JooqContext(
    val create: DSLContext,
    val jdbcConnection: Connection,
)

private class JooqContextInt(
    val ctx: JooqContext
) {
    companion object {
        fun create(jdbcConnection: Connection): JooqContextInt {
            val create = DSL.using(jdbcConnection, SQLDialect.H2)
            return JooqContextInt(JooqContext(create, jdbcConnection))
        }
    }

    internal fun <R> runInTransaction(block: () -> R): R {
        ctx.jdbcConnection.autoCommit = false
        try {
            val result = block()
            ctx.jdbcConnection.commit()
            return result
        } catch (e: Exception) {
            try {
                ctx.jdbcConnection.rollback()
            } catch (rollbackException: SQLException) {
                e.addSuppressed(rollbackException)
            }
            throw e
        }
    }
}

private fun currentJooqContext(): JooqContextInt =
    jooqContextThreadLocal.get() ?: throw IllegalStateException("Not running in transaction; call this function from the db{} block")

/**
 * Returns the current JOOQ [Configuration]. Fails if the current thread is not running in the [db] method.
 */
fun currentConfiguration(): Configuration = currentJooqContext().ctx.create.configuration()
