package com.vaadin.starter.beveragebuddy.backend.simplejooq

import jakarta.validation.Validation
import jakarta.validation.Validator

object SimpleJooq {
    @Volatile
    var validator: Validator = Validation.buildDefaultValidatorFactory().validator
}
