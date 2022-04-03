package com.krivochkov.homework_2.domain.use_cases.user

import com.krivochkov.homework_2.data.repositories.UserRepositoryImpl
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.domain.repositories.UserRepository
import io.reactivex.Observable
import io.reactivex.Single

class LoadAllUsersUseCase(
    private val repository: UserRepository = UserRepositoryImpl()
) {

    operator fun invoke(filter: ((User) -> Boolean)? = null): Single<List<User>> {
        return repository.loadUsers()
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { filter?.invoke(it) ?: true }
            .toList()
    }
}