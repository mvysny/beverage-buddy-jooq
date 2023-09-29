package com.vaadin.starter.beveragebuddy.backend

import eu.vaadinonkotlin.rest.*
import io.javalin.Javalin
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

/**
 * Provides access to person list. To test, just run `curl http://localhost:8080/rest/categories`
 */
@WebServlet(urlPatterns = ["/rest/*"], name = "JavalinRestServlet", asyncSupported = false)
class JavalinRestServlet : HttpServlet() {
    val javalin = Javalin.createStandalone().apply {
        gsonMapper(VokRest.gson)
        get("/rest/categories") { ctx -> ctx.json(Category.findAll()) }
    }.javalinServlet()

    override fun service(req: HttpServletRequest, resp: HttpServletResponse) {
        javalin.service(req, resp)
    }
}
