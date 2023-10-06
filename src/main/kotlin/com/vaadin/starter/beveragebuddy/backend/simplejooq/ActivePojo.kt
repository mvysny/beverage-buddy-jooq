package com.vaadin.starter.beveragebuddy.backend.simplejooq

import jakarta.validation.ConstraintViolationException
import org.jooq.Configuration
import org.jooq.UpdatableRecord
import org.jooq.impl.DAOImpl

/**
 * An Active POJO: it is able to perform basic CRUD operations.
 */
interface ActivePojo<R : UpdatableRecord<R>, THIS : Any, ID : Any> {
    fun dao(cfg: Configuration? = currentConfiguration()): DAOImpl<R, THIS, ID>

    @Suppress("UNCHECKED_CAST")
    private fun self(): THIS = this as THIS

    private fun getId(): ID? = dao(null).getId(self())

    /**
     * Checks if this POJO is persistent (has a non-null ID).
     */
    val isPersistent: Boolean get() = getId() != null

    fun create(validate: Boolean = true) {
        check(!isPersistent) { "POJO already persistent" }
        if (validate) {
            validate()
        }
        db { dao().insert(self()) }
        check(isPersistent) { "ID has not been filled into the POJO" }
    }

    fun save(validate: Boolean = true) {
        if (!isPersistent) {
            create(validate)
        } else {
            if (validate) {
                validate()
            }
            db { dao().merge(self()) }
            check(isPersistent) { "ID has not been filled into the POJO" }
        }
    }

    fun delete() {
        check(isPersistent) { "POJO not yet persistent" }
        db { dao().deleteById(getId()!!) }
    }

    fun validate() {
        val violations = SimpleJooq.validator.validate<Any>(this)
        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }
    }
}
