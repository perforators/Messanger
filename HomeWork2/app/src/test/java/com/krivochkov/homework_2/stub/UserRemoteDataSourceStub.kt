package com.krivochkov.homework_2.stub

import com.krivochkov.homework_2.data.sources.remote.data_sources.UserRemoteDataSource
import com.krivochkov.homework_2.data.sources.remote.dto.PresenceDto
import com.krivochkov.homework_2.data.sources.remote.dto.UserDto
import io.reactivex.Single

class UserRemoteDataSourceStub : UserRemoteDataSource {

    var userProvider: () -> Single<UserDto> = {
        Single.just(UserDto(id = 0, avatar = "", email = "", fullName = "", isBot = false))
    }
    var presenceProvider: () -> Single<PresenceDto> = {
        Single.just(PresenceDto(status = "", timestamp = 0))
    }

    override fun getOwnUser(): Single<UserDto> = userProvider()

    override fun getUserPresence(userEmail: String): Single<PresenceDto> = presenceProvider()

    override fun getAllUsers(): Single<List<UserDto>> {
        throw UnsupportedOperationException()
    }
}