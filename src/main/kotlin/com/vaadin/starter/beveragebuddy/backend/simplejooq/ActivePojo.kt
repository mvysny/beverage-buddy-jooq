package com.vaadin.starter.beveragebuddy.backend.simplejooq

import jakarta.validation.ConstraintViolationException
import org.jooq.Configuration
import org.jooq.UpdatableRecord
import org.jooq.impl.DAOImpl

/**
 * An Active POJO: it is able to perform basic CRUD operations.
 */
interface ActivePojo<R : UpdatableRecord<R>, THIS : Any, ID : Any> {
    /**
     * Returns the DAO to be used when running CRUD operations. By default, this
     * function must be run from within the `db{}` block since it fetches JOOQ
     * [Configuration] from [currentConfiguration].
     */
    fun dao(cfg: Configuration? = currentConfiguration()): DAOImpl<R, THIS, ID>

    @Suppress("UNCHECKED_CAST")
    private fun self(): THIS = this as THIS

    private fun getId(): ID? = dao(null).getId(self())

    /**
     * Checks if this POJO is persistent (has a non-null ID).
     */
    val isPersistent: Boolean get() = getId() != null

    /**
     * Always issues the database `INSERT`, even if the `id` is not null. This is useful for two cases:
     *  * When the entity has a natural ID, such as a NaturalPerson with ID pre-provided by the government (social security number etc),
     *  * ID auto-generated by the application, e.g. UUID
     *
     * It is possible to use this function with entities with IDs auto-generated by the database, but it may be simpler to
     * simply use [save].
     */
    fun create(validate: Boolean = true) {
        if (validate) {
            validate()
        }
        db { dao().insert(self()) }
        check(isPersistent) { "ID has not been filled into the POJO" }
    }

    /**
     * Creates a new row in a database (if `id` is null) or updates the row in a database (if `id` is not null).
     * When creating, this method simply calls the [create] method.
     *
     * It is expected that the database will generate an `id` for us (by sequences,
     * `auto_increment` or other means). That generated ID is then automatically stored into the `id` field.
     *
     * The bean is validated first, by calling [validate].
     * You can bypass this by setting the `validate` parameter to false, but that's not
     * recommended.
     *
     * **WARNING**: if your entity has pre-provided (natural) IDs, you must not call
     * this method with the intent to insert the entity into the database - this method will always run UPDATE and then
     * fail (since nothing has been updated since the row is not in the database yet).
     * To force create the database row, call [create].
     *
     * **INFO**: Entities with IDs created by the application can be made to work properly, by overriding [create]
     * and [create] method accordingly.
     *
     * @throws IllegalStateException if the database didn't provide a new ID (upon new row creation),
     * or if there was no row (if `id` was not null).
     */
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

    /**
     * Deletes this entity from the database. Fails if [isPersistent] is false,
     * since it is expected that the entity is already in the database.
     */
    fun delete() {
        check(isPersistent) { "POJO not yet persistent" }
        db { dao().deleteById(getId()!!) }
    }

    /**
     * Validates current entity. The Java JSR303 validation is performed by default: just add `jakarta.validation`
     * annotations to entity properties.
     *
     * Make sure to add the validation annotations to
     * fields or getters otherwise they will be ignored. For example `@field:NotNull` or `@get:NotNull`.
     *
     * You can override this method to perform additional validations on the level of the entire entity.
     *
     * @throws jakarta.validation.ValidationException when validation fails.
     */
    fun validate() {
        val violations = SimpleJooq.validator.validate<Any>(this)
        if (violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }
    }

    /**
     * Checks whether this entity is valid: calls [validate] and returns false if [ConstraintViolationException] is thrown.
     */
    val isValid: Boolean
        get() = try {
            validate()
            true
        } catch (ex: ConstraintViolationException) {
            false
        }
}
