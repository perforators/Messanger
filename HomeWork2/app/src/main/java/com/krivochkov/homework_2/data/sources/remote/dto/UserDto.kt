package com.krivochkov.homework_2.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("user_id") val id: Long,
    @SerialName("avatar_url") val avatar: String? = null,
    @SerialName("email") val email: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("is_bot") val isBot: Boolean
)