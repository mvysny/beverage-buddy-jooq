package com.vaadin.starter.beveragebuddy.backend.simplejooq

import org.jooq.UpdatableRecord
import org.jooq.impl.DAOImpl

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
