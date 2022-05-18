package com.krivochkov.homework_2.data.mappers

import com.krivochkov.homework_2.data.sources.local.entity.UserEntity
import com.krivochkov.homework_2.data.sources.remote.dto.UserDto
import com.krivochkov.homework_2.domain.models.User

fun UserDto.mapToUser(status: String = "") = User(
    id = id,
    fullName = fullName,
    email = email,
    avatarUrl = avatar,
    status = status
)

fun User.mapToUserEntity(isMe: Boolean = false) = UserEntity(
    id = id,
    fullName = fullName,
    email = email,
    avatar = avatarUrl,
    isMe = isMe
)

fun UserDto.mapToUserEntity(isMe: Boolean = false) = UserEntity(
    id = id,
    fullName = fullName,
    email = email,
    avatar = avatar,
    isMe = isMe
)

fun UserEntity.mapToUser() = User(
    id = id,
    fullName = fullName,
    email = email,
    avatarUrl = avatar,
    status = ""
)