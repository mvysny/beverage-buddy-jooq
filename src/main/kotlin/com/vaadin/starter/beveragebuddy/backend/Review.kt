package com.vaadin.starter.beveragebuddy.backend

import com.github.vokorm.KEntity
import jakarta.validation.constraints.*
import java.time.LocalDate

/**
 * Represents a beverage review.
 * @property name the beverage name
 * @property score the score, 1..5, 1 being worst, 5 being best
 * @property date when the review was done
 * @property category the beverage category [Category.id]. May be null if the category has been deleted.
 * @property count times tasted, 1..99
 */
open class Review(override var id: Long? = null,
                  
                  @field:NotNull
                  @field:Min(1)
                  @field:Max(5)
                  open var score: Int = 1,

                  @field:NotBlank
                  @field:Size(min = 3)
                  open var name: String = "",

                  @field:NotNull
                  @field:PastOrPresent
                  open var date: LocalDate = LocalDate.now(),

                  @field:NotNull
                  open var category: Long? = null,

                  @field:NotNull
                  @field:Min(1)
                  @field:Max(99)
                  open var count: Int = 1) : KEntity<Long> {
    override fun toString() = "${javaClass.simpleName}(id=$id, score=$score, name='$name', date=$date, category=$category, count=$count)"

    fun copy(): Review = Review(id, score, name, date, category, count)
}
