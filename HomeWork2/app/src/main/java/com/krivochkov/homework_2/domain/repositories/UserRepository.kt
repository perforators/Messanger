package com.krivochkov.homework_2.domain.repositories

import com.krivochkov.homework_2.domain.models.User
import io.reactivex.Single

interface UserRepository {

    fun loadUsers(): Single<List<User>>

    fun loadMyUser(): Single<User>
}