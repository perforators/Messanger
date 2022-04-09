package com.krivochkov.homework_2.domain.use_cases.user

import com.krivochkov.homework_2.data.repositories.UserRepositoryImpl
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.domain.repositories.UserRepository
import com.krivochkov.homework_2.domain.use_cases.SearchableUseCase
import io.reactivex.Single

class LoadAllUsersUseCase(
    private val repository: UserRepository = UserRepositoryImpl()
): SearchableUseCase<User> {

    override operator fun invoke(filter: ((User) -> Boolean)?): Single<List<User>> {
        return repository.loadUsers()
            .map { users -> users.filter { filter?.invoke(it) ?: true } }
    }
}