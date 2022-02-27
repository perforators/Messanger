package com.krivochkov.homework1.util

import java.text.SimpleDateFormat
import java.util.*

object TimeConverter {

    private const val DEFAULT_FORMAT_TEMPLATE = "yyyy.MM.dd HH:mm"

    fun convertLongToTime(time: Long, customFormat: String? = null): String {
        val date = Date(time)
        val templateFormat = customFormat ?: DEFAULT_FORMAT_TEMPLATE
        val dateFormat = SimpleDateFormat(templateFormat, Locale.ROOT)
        return dateFormat.format(date)
    }
}