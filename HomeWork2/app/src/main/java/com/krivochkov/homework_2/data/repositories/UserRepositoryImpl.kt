package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.data.mappers.mapToUser
import com.krivochkov.homework_2.data.mappers.mapToUserEntity
import com.krivochkov.homework_2.data.sources.local.data_sources.UserLocalDataSource
import com.krivochkov.homework_2.data.sources.remote.data_sources.UserRemoteDataSource
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.domain.repositories.UserRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {

    override fun getUsers(): Single<List<User>> {
        val allUsers = userRemoteDataSource.getAllUsers()
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { !it.isBot }
            .flatMapSingle { userDto ->
                getUserStatus(userDto.email)
                    .map { userDto.mapToUser(it) }
                    .subscribeOn(Schedulers.io())
                    .retry(COUNT_RETRY)
            }
            .toList()
        val myUser = userRemoteDataSource.getOwnUser()

        return allUsers.zipWith(myUser) { all, my ->
            userLocalDataSource.updateAllUsers(
                all.map { user ->
                    user.mapToUserEntity(isMe = user.id == my.id)
                }
            )
            all
        }
    }

    override fun getCachedUsers(): Single<List<User>> {
        return userLocalDataSource.getAllUsers()
            .map { users -> users.map { it.mapToUser() } }
    }

    override fun getMyUser(): Single<User> {
        return userRemoteDataSource.getOwnUser()
            .flatMap { userDto ->
                userLocalDataSource.updateMyUser(userDto.mapToUserEntity(isMe = true))
                getUserStatus(userDto.email).map { userDto.mapToUser(it) }
            }
    }

    override fun getCachedMyUser(): Single<User> {
        return userLocalDataSource.getMyUser().map { it.mapToUser() }
    }

    private fun getUserStatus(userEmail: String): Single<String> {
        return userRemoteDataSource.getUserPresence(userEmail).map { it.status }
    }

    companion object {
        private const val COUNT_RETRY = 5L
    }
}