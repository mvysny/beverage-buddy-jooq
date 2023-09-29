package com.vaadin.starter.beveragebuddy.backend.simplejooq

import jakarta.validation.Validation
import jakarta.validation.Validator
import javax.sql.DataSource

object SimpleJooq {
    @Volatile
    var validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Volatile
    lateinit var dataSource: DataSource
}
