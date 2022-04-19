package com.krivochkov.homework_2.domain.use_cases.user

import com.krivochkov.homework_2.domain.repositories.UserRepository

class LoadAllUsersUseCase(private val repository: UserRepository) {

    operator fun invoke() = repository.getUsers()
}