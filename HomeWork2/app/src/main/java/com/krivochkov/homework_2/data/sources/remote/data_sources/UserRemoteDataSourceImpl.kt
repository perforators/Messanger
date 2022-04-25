package com.krivochkov.homework_2.data.sources.remote.data_sources

import com.krivochkov.homework_2.data.sources.remote.api.ZulipApi
import com.krivochkov.homework_2.data.sources.remote.dto.PresenceDto
import com.krivochkov.homework_2.data.sources.remote.dto.UserDto
import io.reactivex.Single

class UserRemoteDataSourceImpl(
    private val api: ZulipApi
) : UserRemoteDataSource {

    override fun getAllUsers(): Single<List<UserDto>> {
        return api.getAllUsers().map { it.users }
    }

    override fun getOwnUser(): Single<UserDto> {
        return api.getOwnUser()
    }

    override fun getUserPresence(userEmail: String): Single<PresenceDto> {
        return api.getUserPresence(userEmail).map { it.aggregatedPresence.presence }
    }
}