package com.krivochkov.homework_2.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private val defaultDateFormat = SimpleDateFormat("dd MMM", Locale("ru"))

fun Long.convertToDate(customDateFormat: SimpleDateFormat? = null): String {
    val dateFormat = customDateFormat ?: defaultDateFormat
    val date = Date(TimeUnit.SECONDS.toMillis(this))
    return dateFormat.format(date)
}