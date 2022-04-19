package com.krivochkov.homework_2.data.mappers

import com.krivochkov.homework_2.data.sources.remote.dto.UserDto
import com.krivochkov.homework_2.domain.models.User

fun UserDto.mapToUser(status: String = "") = User(
    id = id,
    fullName = fullName,
    email = email,
    avatarUrl = avatar,
    status = status
)