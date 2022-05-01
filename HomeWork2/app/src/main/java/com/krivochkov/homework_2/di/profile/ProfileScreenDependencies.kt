package com.krivochkov.homework_2.di.profile

import com.krivochkov.homework_2.domain.repositories.UserRepository

interface ProfileScreenDependencies {

    fun getUserRepository(): UserRepository
}