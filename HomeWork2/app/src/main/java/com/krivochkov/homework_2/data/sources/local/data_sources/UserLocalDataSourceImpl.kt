package com.krivochkov.homework_2.data.sources.local.data_sources

import com.krivochkov.homework_2.data.sources.local.dao.UserDao
import com.krivochkov.homework_2.data.sources.local.entity.UserEntity
import io.reactivex.Single
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val dao: UserDao
) : UserLocalDataSource {

    override fun getAllUsers(): Single<List<UserEntity>> {
        return dao.getAllUsers()
    }

    override fun getMyUser(): Single<UserEntity> {
        return dao.getMyUser()
    }

    override fun updateAllUsers(newUsers: List<UserEntity>) {
        dao.updateAllUsers(newUsers)
    }

    override fun updateMyUser(myUser: UserEntity) {
        dao.updateMyUser(myUser)
    }
}