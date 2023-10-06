package com.vaadin.starter.beveragebuddy.backend

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vaadin.starter.beveragebuddy.backend.jooq.tables.references.CATEGORY
import com.vaadin.starter.beveragebuddy.backend.simplejooq.db
import io.javalin.Javalin
import io.javalin.json.JSON_MAPPER_KEY
import io.javalin.json.JsonMapper
import io.javalin.json.PipedStreamUtil
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 * Provides access to person list. To test, just run `curl http://localhost:8080/rest/categories`
 */
@WebServlet(urlPatterns = ["/rest/*"], name = "JavalinRestServlet", asyncSupported = false)
class JavalinRestServlet : HttpServlet() {
    val javalin = Javalin.createStandalone().apply {
        gsonMapper(GsonBuilder().registerJavaTimeAdapters().create())
        get("/rest/categories") { ctx -> ctx.json(db { CATEGORY.dao.findAll() }) }
    }.javalinServlet()

    override fun service(req: HttpServletRequest, resp: HttpServletResponse) {
        javalin.service(req, resp)
    }
}

private fun GsonBuilder.registerJavaTimeAdapters(): GsonBuilder = apply {
    Converters.registerAll(this)
}

/**
 * Configures [gson] as Javalin's [JsonMapper].
 * @param charset which encoding to use, defaults to UTF-8
 */
fun Javalin.gsonMapper(gson: Gson, charset: Charset = Charsets.UTF_8) {
    attribute(JSON_MAPPER_KEY, GsonMapper(gson, charset))
}

/**
 * Implements Javalin's [JsonMapper] on top of [Gson].
 * @property charset which encoding to use, defaults to UTF-8
 */
class GsonMapper(val gson: Gson, val charset: Charset = Charsets.UTF_8) : JsonMapper {
    override fun toJsonString(obj: Any, type: Type): String {
        return when (obj) {
            is String -> obj // the default mapper treats strings as if they are already JSON
            else -> gson.toJson(obj) // convert object to JSON
        }
    }

    override fun toJsonStream(obj: Any, type: Type): InputStream {
        return when (obj) {
            is String -> obj.byteInputStream() // the default mapper treats strings as if they are already JSON
            else -> PipedStreamUtil.getInputStream { pipedOutputStream ->
                gson.toJson(obj, OutputStreamWriter(pipedOutputStream, charset))
            }
        }
    }

    override fun <T : Any> fromJsonString(
        json: String,
        targetType: Type
    ): T = gson.fromJson(json, targetType)

    override fun <T : Any> fromJsonStream(
        json: InputStream,
        targetType: Type
    ): T = gson.fromJson(InputStreamReader(json, charset), targetType)
}
