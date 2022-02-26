package com.krivochkov.homework1.util

import android.content.res.Resources
import com.krivochkov.homework1.R
import java.text.SimpleDateFormat
import java.util.*

fun Long.convertLongToTime(): String {
    val date = Date(this)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.ROOT)
    return format.format(date)
}

fun Resources.getDefaultEventTitle(eventId: String): String {
    return String.format(
        getString(R.string.default_title),
        eventId
    )
}