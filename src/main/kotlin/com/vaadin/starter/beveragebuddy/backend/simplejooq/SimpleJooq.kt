package com.vaadin.starter.beveragebuddy.backend.simplejooq

import jakarta.validation.Validation
import jakarta.validation.Validator
import javax.sql.DataSource

object SimpleJooq {
    /**
     * The jakarta.validation validator.
     */
    @Volatile
    var validator: Validator = Validation.buildDefaultValidatorFactory().validator

    /**
     * The [db] function obtains the JDBC connection from here. Use HikariCP connection pooling.
     */
    @Volatile
    lateinit var dataSource: DataSource
}
