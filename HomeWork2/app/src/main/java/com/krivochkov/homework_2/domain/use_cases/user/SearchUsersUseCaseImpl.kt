package com.krivochkov.homework_2.domain.use_cases.user

import com.krivochkov.homework_2.domain.models.User
import io.reactivex.Single
import javax.inject.Inject

class SearchUsersUseCaseImpl @Inject constructor(
    private val loadAllUsersUseCase: LoadAllUsersUseCase
) : SearchUsersUseCase {

    override operator fun invoke(query: String): Single<List<User>> =
        loadAllUsersUseCase()
            .map { users -> users.filter { it.fullName.contains(query) } }
}
