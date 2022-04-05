package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.data.sources.remote.data_sources.UserRemoteDataSourceImpl
import com.krivochkov.homework_2.data.sources.remote.data_sources.UserRemoteDataSource
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.domain.repositories.UserRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource = UserRemoteDataSourceImpl()
) : UserRepository {

    override fun loadUsers(): Single<List<User>> {
        return userRemoteDataSource.getAllUsers()
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { !it.isBot }
            .flatMapSingle { userDto ->
                getUserStatus(userDto.email)
                    .map { userDto.toUser(it) }
                    .subscribeOn(Schedulers.io())
                    .retry(COUNT_RETRY) // пока не смог придумать,
                // как ограничить запросы на получение статуса,
                // т.к. сервер выдаёт ошибку, если много флудишь запросами.
                // Без параллельности общий запрос на пользователей идёт значительно дольше
            }
            .toList()
    }

    override fun loadMyUser(): Single<User> {
        return userRemoteDataSource.getOwnUser()
            .flatMap { userDto -> getUserStatus(userDto.email).map { userDto.toUser(it) } }
    }

    private fun getUserStatus(userEmail: String): Single<String> {
        return userRemoteDataSource.getUserPresence(userEmail).map { it.status }
    }

    companion object {
        private const val COUNT_RETRY = 5L
    }
}