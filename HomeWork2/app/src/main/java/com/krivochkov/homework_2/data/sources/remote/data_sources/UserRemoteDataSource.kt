package com.krivochkov.homework_2.data.sources.remote.data_sources

import com.krivochkov.homework_2.data.sources.remote.dto.PresenceDto
import com.krivochkov.homework_2.data.sources.remote.dto.UserDto
import io.reactivex.Single

interface UserRemoteDataSource {

    fun getAllUsers(): Single<List<UserDto>>

    fun getOwnUser(): Single<UserDto>

    fun getUserPresence(userEmail: String): Single<PresenceDto>
}