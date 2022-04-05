package com.krivochkov.homework_2.domain.models

data class User(
    val id: Long,
    val fullName: String,
    val email: String,
    val avatarUrl: String?,
    val status: String
)