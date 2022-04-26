package com.krivochkov.homework_2.domain.use_cases.user

import com.krivochkov.homework_2.domain.models.User
import io.reactivex.Single

class SearchUsersUseCase(private val loadAllUsersUseCase: LoadAllUsersUseCase) {

    operator fun invoke(query: String): Single<List<User>> =
        loadAllUsersUseCase()
            .map { users -> users.filter { it.fullName.contains(query) } }
}