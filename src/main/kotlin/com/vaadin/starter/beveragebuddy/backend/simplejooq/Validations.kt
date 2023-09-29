package com.vaadin.starter.beveragebuddy.backend.simplejooq

import jakarta.validation.ConstraintViolationException
import org.jooq.Record

val Record.isValid: Boolean
    get() = try {
        validate()
        true
    } catch (ex: ConstraintViolationException) {
        false
    }

fun Record.validate() {
    val violations = SimpleJooq.validator.validate<Any>(this)
    if (violations.isNotEmpty()) {
        throw ConstraintViolationException(violations)
    }
}
