package com.krivochkov.homework1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarEvent(
    val id: String,
    val title: String,
    val startDate: String,
    val endDate: String,
): Parcelable