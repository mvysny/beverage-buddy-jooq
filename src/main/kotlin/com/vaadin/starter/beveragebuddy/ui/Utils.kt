package com.vaadin.starter.beveragebuddy.ui

import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.Result
import com.vaadin.flow.data.binder.ValueContext
import com.vaadin.flow.data.converter.Converter
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.provider.Query
import org.slf4j.LoggerFactory

class IntToByteConverter : Converter<Int?, Byte> {
    override fun convertToModel(
        value: Int?,
        context: ValueContext
    ): Result<Byte?> = when {
        value == null -> Result.ok(null)
        value in -128..127 -> Result.ok(value.toByte())
        else -> Result.error("Must be -128..127")
    }

    override fun convertToPresentation(
        value: Byte?,
        context: ValueContext
    ): Int? = value?.toInt()
}

fun <BEAN> Binder.BindingBuilder<BEAN, Int?>.toByte(): Binder.BindingBuilder<BEAN, Byte?> =
    withConverter(IntToByteConverter())

fun <T, F> DataProvider<T, F>.getSize(filter: F? = null): Int = size(Query(filter))

fun AutoCloseable.closeQuietly() {
    try {
        close()
    } catch (e: Exception) {
        LoggerFactory.getLogger(javaClass).warn("Failed to close $this", e)
    }
}
