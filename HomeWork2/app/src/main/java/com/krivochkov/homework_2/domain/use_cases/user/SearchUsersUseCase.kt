package com.krivochkov.homework_2.domain.use_cases.user

import com.krivochkov.homework_2.domain.models.User
import io.reactivex.Single

interface SearchUsersUseCase {

    operator fun invoke(query: String): Single<List<User>>
}