package com.vaadin.starter.beveragebuddy.backend

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.GsonBuilder
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.http4k.core.ContentType
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.servlet.jakarta.Http4kJakartaServletAdapter

object RestService {
    val gson = GsonBuilder().registerJavaTimeAdapters().create()
    private fun Response.json(json: Any): Response = header("Content-Type", ContentType.APPLICATION_JSON.toHeaderValue())
        .body(gson.toJson(json))

    val app: RoutingHttpHandler = routes(
        "rest/categories" bind GET to { Response(OK).json(db { CATEGORY.dao.findAll() }) }
    )
}

/**
 * Provides access to person list. To test, just run `curl http://localhost:8080/rest/categories`
 */
@WebServlet(urlPatterns = ["/rest/*"], name = "RestServlet", asyncSupported = false)
class RestServlet : HttpServlet() {
    private val adapter = Http4kJakartaServletAdapter(RestService.app)
    override fun service(req: HttpServletRequest, resp: HttpServletResponse) = adapter.handle(req, resp)
}

private fun GsonBuilder.registerJavaTimeAdapters(): GsonBuilder = apply {
    Converters.registerAll(this)
}
