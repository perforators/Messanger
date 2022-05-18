package com.krivochkov.homework_2.domain.repositories

import com.krivochkov.homework_2.domain.models.User
import io.reactivex.Single

interface UserRepository {

    fun getUsers(): Single<List<User>>

    fun getCachedUsers(): Single<List<User>>

    fun getMyUser(): Single<User>

    fun getCachedMyUser(): Single<User>
}