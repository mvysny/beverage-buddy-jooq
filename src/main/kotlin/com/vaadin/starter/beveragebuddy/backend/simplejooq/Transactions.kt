package com.vaadin.starter.beveragebuddy.backend.simplejooq

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.UpdatableRecord
import org.jooq.impl.DSL
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.SQLException

/**
 * Holds reference to the current JDBC connection when running in the `db{}` block.
 */
private val jooqContextThreadLocal = ThreadLocal<JooqContextInt>()

/**
 * Makes sure given block is executed in a DB transaction. When the block finishes normally, the transaction commits;
 * if the block throws any exception, the transaction is rolled back.
 *
 * Example of use: `db { create.query("yada yada") }`
 * @param block the block to run in the transaction.
 */
fun <R> db(block: JooqContext.() -> R): R {
    var ctx: JooqContextInt? = jooqContextThreadLocal.get()
    if (ctx != null) {
        // already in a transaction - nested call to db{}. Simply run the block right away.
        return ctx.ctx.block()
    }

    // no transaction. Obtain the JDBC connection and create the context.
    val jdbcConnection = SimpleJooq.dataSource.connection
    ctx = JooqContextInt.create(jdbcConnection)

    jooqContextThreadLocal.set(ctx)
    try {
        return ctx.runInTransaction { ctx.ctx.block() }
    } finally {
        jooqContextThreadLocal.set(null)
        ctx.close()
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
) : AutoCloseable {
    private val attachedRecords = mutableListOf<UpdatableRecord<*>>()

    fun attach(record: UpdatableRecord<*>) {
        record.attach(ctx.create.configuration())
        attachedRecords.add(record)
    }

    override fun close() {
        attachedRecords.forEach { it.detach() }
        attachedRecords.clear()
        try {
            ctx.jdbcConnection.close()
        } catch (e: SQLException) {
            log.warn("Failed to close the JDBC Connection", e)
        }
    }

    companion object {
        @JvmStatic
        internal val log = LoggerFactory.getLogger(JooqContext::class.java)

        fun create(jdbcConnection: Connection): JooqContextInt {
            val create = DSL.using(jdbcConnection, SQLDialect.H2)
            // we don't want Records to carry the connection aroun since it will be
            // invalidated by the pool once the db2{} block ends.
            create.configuration().settings().withAttachRecords(false)
            return JooqContextInt(JooqContext(create, jdbcConnection))
        }
    }

    internal fun <R> runInTransaction(block: () -> R): R {
        ctx.jdbcConnection.autoCommit = false
        var success = false
        try {
            val result = block()
            ctx.jdbcConnection.commit()
            success = true
            return result
        } finally {
            if (!success) {
                try {
                    ctx.jdbcConnection.rollback()
                } catch (e: SQLException) {
                    log.error("Failed to roll back the transaction", e)
                }
            }
        }
    }
}

private fun currentJooqContext(): JooqContextInt =
    jooqContextThreadLocal.get() ?: throw IllegalStateException("Not running in transaction; call this function from the db{} block")

/**
 * Attaches given record to this transaction so that you can call [UpdatableRecord.store] on it. Must be called from within the `db{}` block.
 *
 * Example of use:
 * ```kotlin
 * db2 { Category(name = "Foo").attach().insert() }
 * ```
 */
fun <R: UpdatableRecord<R>> R.attach(): R {
    currentJooqContext().attach(this)
    return this
}
