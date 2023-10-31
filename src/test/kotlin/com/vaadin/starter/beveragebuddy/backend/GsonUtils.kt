package com.vaadin.starter.beveragebuddy.backend

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.http4k.core.Body
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Type

/**
 * Parses [json] as a list of items with class [itemClass] and returns that.
 */
fun <T> Gson.fromJsonArray(json: String, itemClass: Class<T>): List<T> =
    fromJsonArray(StringReader(json), itemClass)

/**
 * Parses JSON from a [reader] as a list of items with class [itemClass] and returns that.
 */
fun <T> Gson.fromJsonArray(reader: Reader, itemClass: Class<T>): List<T> {
    val type: Type = TypeToken.getParameterized(List::class.java, itemClass).type
    return fromJson<List<T>>(reader, type)
}

/**
 * Parses the response as a JSON and converts it to a Java object using [gson].
 */
fun <T> Body.json(gson: Gson, clazz: Class<T>): T = gson.fromJson(stream.buffered().reader(), clazz)

/**
 * Parses the response as a JSON array and converts it into a list of Java object with given [clazz] using [gson].
 */
fun <T> Body.jsonArray(gson: Gson, clazz: Class<T>): List<T> = gson.fromJsonArray(stream.buffered().reader(), clazz)

/**
 * Parses the response as a JSON array and converts it into a list of Java object with given type [T] using [gson].
 */
inline fun <reified T> Body.jsonArray(gson: Gson): List<T> = jsonArray(gson, T::class.java)
