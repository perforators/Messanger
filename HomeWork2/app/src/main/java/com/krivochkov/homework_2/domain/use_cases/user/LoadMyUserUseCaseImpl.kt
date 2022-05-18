package com.krivochkov.homework_2.domain.use_cases.user

import com.krivochkov.homework_2.domain.repositories.UserRepository
import javax.inject.Inject

class LoadMyUserUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : LoadMyUserUseCase {

    override operator fun invoke(cached: Boolean) = if (cached) {
        repository.getCachedMyUser()
    } else {
        repository.getMyUser()
    }
}