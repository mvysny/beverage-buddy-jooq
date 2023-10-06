package com.vaadin.starter.beveragebuddy.backend.simplejooq

import com.vaadin.starter.beveragebuddy.ui.fetchOneInt
import org.jooq.Record
import org.jooq.Table
import org.jooq.TableField
import org.jooq.UpdatableRecord
import org.jooq.impl.DAOImpl

/**
 * Sometimes you don't want a class to be an entity for some reason (e.g. when it doesn't have a primary key),
 * but still it's mapped to a table and you would want to have Dao support for that class.
 * Just add an extension function to your table, e.g.
 * ```
 * val Category.dao: DaoOfAny<CategoryRecord> get() = DaoOfAny(Category.CATEGORY)
 * ```
 * You can now use `CATEGORY.dao.findAll()`, `CATEGORY.dao.count()` and other lovely methods.
 * @param <R> the type of the record provided by this Dao
 */
open class DaoOfAny<R : Record>(protected val table: Table<R>) {
    /**
     * Finds all rows in given table. The list is eager,
     * therefore it's useful for small-ish tables only.
     */
    open fun findAll(): List<R> =
        db { create.selectFrom(table).fetch() }


    /**
     * Retrieves the single entity from this table. Useful for example for config table which hosts one row only.
     * Returns null if there is no such entity; fails if there are two or more entities matching the criteria.
     *
     * This function returns null if there is no item matching. Use [single]
     * if you wish to throw an exception in case that the entity does not exist.
     *
     * @throws IllegalStateException if there are two or more rows.
     */
    open fun findOne(): R? = db { create.fetchOne(table) }

    /**
     * Retrieves the single entity from this table. Useful for example for config table which hosts one row only.
     * Fails if there is no entity, or if there are two or more entities matching the criteria.
     *
     * This function fails if there is no item matching. Use [findOne]
     * if you wish to return `null` in case that the entity does not exist.
     *
     * @throws IllegalStateException if the table is empty, or if there are two or more rows.
     */
    open fun single(): R = db { create.fetchSingle(table) }

    /**
     * Retrieves single random entity. Returns null if there is no such entity.
     */
    open fun findFirst(): R? = db { create.selectFrom(table).offset(0).limit(1).fetch().firstOrNull() }

    /**
     * Deletes all rows from this database table.
     */
    open fun deleteAll() {
        db {
            create.deleteFrom(table).execute()
        }
    }

    /**
     * Counts all rows in this table.
     */
    open fun count(): Int = db {
        create.selectCount().from(table).fetchOneInt()
    }

    /**
     * Checks whether there exists any row in this table.
     */
    open fun existsAny(): Boolean = findFirst() != null
}

/**
 * Data access object, provides instances of given {@link Entity}.
 * Just add an extension function to your table, e.g.
 * ```
 * val Category.dao: Dao<CategoryRecord, Long> get() = Dao(Category.CATEGORY)
 * ```
 * You can now use `Person.dao.findAll()`, `Person.dao.get(25)` and other nice methods :)
 *
 * @param <T> the type of the {@link Entity} provided by this Dao
 * @param <ID> the type of {@link Entity} ID.
 */
open class Dao<R: Record, ID: Any>(table: Table<R>): DaoOfAny<R>(table) {
    protected val idField: TableField<R, ID>

    init {
        val pk = requireNotNull(table.primaryKey) { "$table has no primary key" }
        require(pk.fields.size == 1) { "$table doesn't have exactly one column for the primary key: ${pk.fields}" }
        @Suppress("UNCHECKED_CAST")
        idField = pk.fields[0] as TableField<R, ID>
    }

    /**
     * Retrieves entity with given [id]. Fails if there is no such entity.
     * @throws IllegalStateException if there is no entity with given id.
     */
    open fun getById(id: ID): R =
        checkNotNull(findById(id)) { "There is no record $table for ID $id" }

    /**
     * Retrieves entity with given [id]. Returns `null` if there is no such entity.
     */
    open fun findById(id: ID): R? = db { create.fetchOne(table, idField.eq(id)) }

    /**
     * Checks whether there exists any row with given id.
     */
    open fun existsById(id: ID): Boolean = findById(id) != null

    /**
     * Deletes row with given ID. Does nothing if there is no such row.
     */
    open fun deleteById(id: ID) {
        db {
            create.deleteFrom(table).where(idField.eq(id)).execute()
        }
    }

    /**
     * Deletes given [record].
     */
    open fun delete(record: R) {
        deleteById(record.get(idField)!!)
    }
}

/**
 * Retrieves entity with given [id]. Fails if there is no such entity.
 * @throws IllegalStateException if there is no entity with given id.
 */
fun <R : UpdatableRecord<R>, POJO: Any, ID: Any> DAOImpl<R, POJO, ID>.getById(id: ID): POJO =
    checkNotNull(findById(id)) { "There is no record $table for ID $id" }

/**
 * Retrieves the single entity from this table. Useful for example for config table which hosts one row only.
 * Fails if there is no entity, or if there are two or more entities matching the criteria.
 *
 * This function fails if there is no item matching. Use [findOne]
 * if you wish to return `null` in case that the entity does not exist.
 *
 * @throws IllegalStateException if the table is empty, or if there are two or more rows.
 */
fun <R : UpdatableRecord<R>, POJO : Any, ID : Any> DAOImpl<R, POJO, ID>.single(): POJO =
    db {
        val single = create.fetchSingle(table)
        mapper().map(single)!!
    }

/**
 * Retrieves single random entity. Returns null if there is no such entity.
 */
fun <R : UpdatableRecord<R>, POJO : Any, ID : Any> DAOImpl<R, POJO, ID>.findFirst(): POJO? =
    db {
        val r = create.selectFrom(table).offset(0).limit(1).fetch().firstOrNull()
        if (r == null) null else mapper().map(r)!!
    }

/**
 * Deletes all rows from this database table.
 */
fun <R : UpdatableRecord<R>, POJO : Any, ID : Any> DAOImpl<R, POJO, ID>.deleteAll() {
    db {
        create.deleteFrom(table).execute()
    }
}
