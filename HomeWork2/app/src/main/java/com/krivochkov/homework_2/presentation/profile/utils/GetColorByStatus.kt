package com.krivochkov.homework_2.presentation.profile.utils

import android.content.Context
import com.krivochkov.homework_2.R

fun getColorByStatus(status: String, context: Context): Int {
    return when (status) {
        ACTIVE_STATUS -> context.getColor(R.color.online_status)
        IDLE_STATUS -> context.getColor(R.color.idle_status)
        OFFLINE_STATUS -> context.getColor(R.color.offline_status)
        else -> context.getColor(R.color.offline_status)
    }
}
