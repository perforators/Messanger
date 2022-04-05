package com.krivochkov.homework_2.data.sources.remote.responses

import com.krivochkov.homework_2.data.sources.remote.dto.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    @SerialName("members") val users: List<UserDto>
)