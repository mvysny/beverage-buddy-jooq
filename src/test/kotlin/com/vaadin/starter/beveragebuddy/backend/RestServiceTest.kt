package com.vaadin.starter.beveragebuddy.backend

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.pojos.Category
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import com.vaadin.starter.beveragebuddy.ui.usingApp
import org.eclipse.jetty.ee10.webapp.WebAppContext
import org.eclipse.jetty.server.Server
import java.net.http.HttpClient
import kotlin.test.expect

/**
 * Uses the VoK `vok-rest-client` module for help with testing of the REST endpoints. See docs on the
 * [vok-rest-client](https://github.com/mvysny/vaadin-on-kotlin/tree/master/vok-rest-client) module for more details.
 */
class PersonRestClient(val baseUrl: String) {
    init {
        require(!baseUrl.endsWith("/")) { "$baseUrl must not end with a slash" }
    }
    private val client: HttpClient = HttpClient.newHttpClient()
    fun getAllCategories(): String {
        val request = "$baseUrl/categories".buildUrl().buildRequest()
        return client.exec(request) { response -> response.body().reader().readText() }
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
        expect("[]") { client.getAllCategories() }
    }
    test("one category") {
        db { CATEGORY.dao.insert(Category(name = "Foo")) }
        expectMatch("""\[\{"id":.+,"name":"Foo"}]""".toRegex()) { client.getAllCategories() }
    }
})

fun expectMatch(regex: Regex, actualBlock: () -> String) {
    val actual = actualBlock()
    expect(true, "$actual doesn't match $regex") { regex.matches(actual) }
}
