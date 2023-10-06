package com.vaadin.starter.beveragebuddy.backend.simplejooq

import jakarta.validation.ConstraintViolationException
import org.jooq.UpdatableRecord
import org.jooq.impl.DAOImpl

/**
 * An Active POJO: it is able to perform basic CRUD operations.
 */
interface ActivePojo<R : UpdatableRecord<R>, THIS : Any, ID : Any> {
    val dao: DAOImpl<R, THIS, ID>

    @Suppress("UNCHECKED_CAST")
    private fun self(): THIS = this as THIS

    /**
     * Checks if this POJO is persistent (has a non-null ID).
     */
    val isPersistent: Boolean get() = dao.getId(self()) != null

    fun create(validate: Boolean = true) {
        check(!isPersistent) { "POJO already persistent" }
        if (validate) {
            validate()
        }
        db { dao.insert(self()) }
    }

    fun save(validate: Boolean = true) {
        if (validate) {
            validate()
        }
        db { dao.merge(self()) }
    }

    fun delete() {
        check(isPersistent) { "POJO already persistent" }
        db { dao.deleteById(dao.getId(self())) }
    }

    fun validate() {
        val violations = SimpleJooq.validator.validate<Any>(this)
        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }
    }
}
