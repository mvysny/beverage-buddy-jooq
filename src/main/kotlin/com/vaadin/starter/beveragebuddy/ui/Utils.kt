package com.vaadin.starter.beveragebuddy.ui

import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.Result
import com.vaadin.flow.data.binder.ValueContext
import com.vaadin.flow.data.converter.Converter

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
