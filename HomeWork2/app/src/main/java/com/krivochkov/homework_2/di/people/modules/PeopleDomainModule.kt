package com.krivochkov.homework_2.di.people.modules

import com.krivochkov.homework_2.domain.use_cases.user.LoadAllUsersUseCase
import com.krivochkov.homework_2.domain.use_cases.user.LoadUsersUseCase
import com.krivochkov.homework_2.domain.use_cases.user.SearchUsersUseCase
import com.krivochkov.homework_2.domain.use_cases.user.SearchUsersUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface PeopleDomainModule {

    @Binds
    fun bindLoadAllUsersUseCase(impl: LoadAllUsersUseCase): LoadUsersUseCase

    @Binds
    fun bindSearchUsersUseCase(impl: SearchUsersUseCaseImpl): SearchUsersUseCase
}