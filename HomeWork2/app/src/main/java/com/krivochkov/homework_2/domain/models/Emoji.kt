package com.krivochkov.homework_2.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Emoji(val name: String, val code: String)