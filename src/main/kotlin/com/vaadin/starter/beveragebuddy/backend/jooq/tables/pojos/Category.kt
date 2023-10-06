/*
 * This file is generated by jOOQ.
 */
package com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos


import com.vaadin.starter.beveragebuddy.backend.CategoryDaoExt
import com.vaadin.starter.beveragebuddy.backend.dao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.daos.CategoryDao
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.records.CategoryRecord
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.simplejooq.ActivePojo
import com.vaadin.starter.beveragebuddy.backend.simplejooq.currentConfiguration
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.jooq.Configuration
import org.jooq.impl.DAOImpl

import java.io.Serializable


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class Category(
    var id: Long? = null,
    @get:Size(max = 200)
    @get:NotBlank
    var name: String? = null
): Serializable, ActivePojo<CategoryRecord, Category, Long> {

    override fun dao(cfg: Configuration?): DAOImpl<CategoryRecord, Category, Long> =
        CategoryDaoExt(cfg)

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (this::class != other::class)
            return false
        val o: Category = other as Category
        if (this.id == null) {
            if (o.id != null)
                return false
        }
        else if (this.id != o.id)
            return false
        if (this.name == null) {
            if (o.name != null)
                return false
        }
        else if (this.name != o.name)
            return false
        return true
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (if (this.id == null) 0 else this.id.hashCode())
        result = prime * result + (if (this.name == null) 0 else this.name.hashCode())
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder("Category (")

        sb.append(id)
        sb.append(", ").append(name)

        sb.append(")")
        return sb.toString()
    }
}