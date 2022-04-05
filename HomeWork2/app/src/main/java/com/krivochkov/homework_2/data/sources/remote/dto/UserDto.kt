package com.krivochkov.homework_2.data.sources.remote.dto

import com.krivochkov.homework_2.domain.models.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("user_id") val id: Long,
    @SerialName("avatar_url") val avatar: String? = null,
    @SerialName("email") val email: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("is_bot") val isBot: Boolean
) {

    fun toUser(status: String = "") = User(
        id = id,
        fullName = fullName,
        email = email,
        avatarUrl = avatar,
        status = status
    )
}