package com.krivochkov.homework_2.data.sources.local.data_sources

import com.krivochkov.homework_2.data.sources.local.entity.UserEntity
import io.reactivex.Single

interface UserLocalDataSource {

    fun getAllUsers(): Single<List<UserEntity>>

    fun getMyUser(): Single<UserEntity>

    fun updateAllUsers(newUsers: List<UserEntity>)

    fun updateMyUser(myUser: UserEntity)
}