package com.vaadin.starter.beveragebuddy.backend

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.ui.usingApp
import org.http4k.core.*
import org.http4k.filter.ClientFilters
import java.io.FileNotFoundException
import java.io.IOException

private fun Response.checkOk(request: Request) {
    if (!status.successful) {
        val msg = "$request ====> $this"
        if (status.code == 404) throw FileNotFoundException(msg)
        throw IOException(msg)
    }
}

val CheckOk = Filter { next -> { next(it).apply { checkOk(it) } } }

fun Request.accept(contentType: ContentType): Request =
    header("Accept", contentType.toHeaderValue())

fun Request.acceptJson(): Request = accept(ContentType.APPLICATION_JSON)

class PersonRestClient {
    private val client: HttpHandler = ClientFilters.FollowRedirects()
        .then(CheckOk)
        .then(RestService.app)
    private val gson = RestService.gson

    fun getAllCategories(): List<Category> {
        val request = Request(Method.GET, "categories").acceptJson()
        return client(request).use { it.body.jsonArray<Category>(gson) }
    }
}

/**
 * The REST test.
 */
class RestServiceTest : DynaTest({
    usingApp()

    lateinit var client: PersonRestClient
    beforeEach { client = PersonRestClient() }

    test("categories smoke test") {
        expectList() { client.getAllCategories() }
    }
    test("one category") {
        db { CATEGORY.dao.insert(Category(name = "Foo")) }
        expectList("Foo") { client.getAllCategories().map { it.name } }
    }
})
