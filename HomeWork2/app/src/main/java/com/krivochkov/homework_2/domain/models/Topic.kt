package com.krivochkov.homework_2.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic(val id: Long, val name: String, val countMessages: Int): Parcelable