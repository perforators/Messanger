package com.krivochkov.homework_2.domain.repositories

import com.krivochkov.homework_2.domain.models.User

interface UserRepository {

    fun loadUsers(): List<User>

    fun loadMyUser(): User
}