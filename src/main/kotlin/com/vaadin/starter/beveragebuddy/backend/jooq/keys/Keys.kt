/*
 * This file is generated by jOOQ.
 */
package com.vaadin.starter.beveragebuddy.backend.jooq.keys


import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.FlywaySchemaHistory
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.Review
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.FlywaySchemaHistoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.ReviewRecord

import org.jooq.ForeignKey
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val CONSTRAINT_3: UniqueKey<CategoryRecord> = Internal.createUniqueKey(Category.CATEGORY, DSL.name("CONSTRAINT_3"), arrayOf(Category.CATEGORY.ID), true)
val FLYWAY_SCHEMA_HISTORY_PK: UniqueKey<FlywaySchemaHistoryRecord> = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, DSL.name("flyway_schema_history_pk"), arrayOf(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK), true)
val CONSTRAINT_8: UniqueKey<ReviewRecord> = Internal.createUniqueKey(Review.REVIEW, DSL.name("CONSTRAINT_8"), arrayOf(Review.REVIEW.ID), true)

// -------------------------------------------------------------------------
// FOREIGN KEY definitions
// -------------------------------------------------------------------------

val CONSTRAINT_8F: ForeignKey<ReviewRecord, CategoryRecord> = Internal.createForeignKey(Review.REVIEW, DSL.name("CONSTRAINT_8F"), arrayOf(Review.REVIEW.CATEGORY), com.vaadin.starter.beveragebuddy.backend.jooq.keys.CONSTRAINT_3, arrayOf(Category.CATEGORY.ID), true)
