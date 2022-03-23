package com.krivochkov.homework_2.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Channel(val id: Long, val name: String, val description: String): Parcelable