package com.vaadin.starter.beveragebuddy.backend

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.expectList
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.ui.usingApp
import org.eclipse.jetty.ee10.webapp.WebAppContext
import org.eclipse.jetty.server.Server
import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.filter.ClientFilters
import kotlin.test.expect

/**
 * Uses the VoK `vok-rest-client` module for help with testing of the REST endpoints. See docs on the
 * [vok-rest-client](https://github.com/mvysny/vaadin-on-kotlin/tree/master/vok-rest-client) module for more details.
 */
class PersonRestClient(val baseUrl: String) {
    init {
        require(!baseUrl.endsWith("/")) { "$baseUrl must not end with a slash" }
    }
    private val client: HttpHandler = ClientFilters.SetBaseUriFrom(Uri.of(baseUrl)).then(JavaHttpClient())
    private val gson = GsonBuilder().registerJavaTimeAdapters().create()
    fun getAllCategoriesAsString(): String {
        val request = Request(Method.GET, "categories")
        return client(request).use { it.bodyString() }
    }
    fun getAllCategories(): List<Category> {
        val request = Request(Method.GET, "categories")
        return client(request).use { it.body.jsonArray<Category>(gson) }
    }
}

@DynaTestDsl
fun DynaNodeGroup.usingJavalin() {
    lateinit var server: Server
    beforeGroup {
        val ctx = WebAppContext()
        // This used to be EmptyResource, but it got removed in Jetty 12. Let's use some dummy resource instead.
        ctx.baseResource = ctx.resourceFactory.newClassLoaderResource("java/lang/String.class")
        ctx.addServlet(JavalinRestServlet::class.java, "/rest/*")
        server = Server(9876)
        server.handler = ctx
        server.start()
    }
    afterGroup { server.stop() }
}

/**
 * The REST test. It bootstraps the app, then it starts Javalin with Jetty so that we can access it via the
 * [PersonRestClient].
 */
class RestServiceTest : DynaTest({
    usingApp()
    usingJavalin()

    lateinit var client: PersonRestClient
    beforeEach { client = PersonRestClient("http://localhost:9876/rest") }

    test("categories smoke test") {
        expectList() { client.getAllCategories() }
    }
    test("one category") {
        db { CATEGORY.dao.insert(Category(name = "Foo")) }
        expectMatch("""\[\{"id":.+,"name":"Foo"}]""".toRegex()) { client.getAllCategoriesAsString() }
    }
})

fun expectMatch(regex: Regex, actualBlock: () -> String) {
    val actual = actualBlock()
    expect(true, "$actual doesn't match $regex") { regex.matches(actual) }
}
