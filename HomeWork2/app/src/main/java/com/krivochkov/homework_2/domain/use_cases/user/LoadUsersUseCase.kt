package com.krivochkov.homework_2.domain.use_cases.user

import com.krivochkov.homework_2.domain.models.User
import io.reactivex.Single

interface LoadUsersUseCase {

    operator fun invoke(cached: Boolean = false): Single<List<User>>
}