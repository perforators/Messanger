package com.krivochkov.homework_2.domain.use_cases.user

import com.krivochkov.homework_2.domain.repositories.UserRepository
import javax.inject.Inject

class LoadAllUsersUseCase @Inject constructor(
    private val repository: UserRepository
) : LoadUsersUseCase {

    override operator fun invoke(cached: Boolean) = if (cached) {
        repository.getCachedUsers()
    } else {
        repository.getUsers()
    }
}