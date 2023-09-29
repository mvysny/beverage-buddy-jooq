package com.vaadin.starter.beveragebuddy.backend

import com.github.mvysny.vokdataloader.DataLoader
import com.github.mvysny.vokdataloader.withFilter
import com.github.vokorm.*
import com.github.vokorm.dataloader.SqlDataLoader
import com.gitlab.mvysny.jdbiorm.Dao
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.time.LocalDate
import jakarta.validation.constraints.*

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

    companion object : Dao<Review, Long>(Review::class.java) {
        /**
         * Computes the total sum of [count] for all reviews belonging to given [categoryId].
         * @return the total sum, 0 or greater.
         */
        fun getTotalCountForReviewsInCategory(categoryId: Long): Long = db {
            handle.createQuery("select sum(r.count) from Review r where r.category = :catId")
                    .bind("catId", categoryId)
                    .mapTo(Long::class.java).one() ?: 0L
        }
    }
}
