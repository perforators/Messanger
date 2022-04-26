package com.krivochkov.homework_2.di.people.modules

import com.krivochkov.homework_2.domain.repositories.UserRepository
import com.krivochkov.homework_2.domain.use_cases.user.LoadAllUsersUseCase
import com.krivochkov.homework_2.domain.use_cases.user.SearchUsersUseCase
import dagger.Module
import dagger.Provides

@Module
class PeopleDomainModule {

    @Provides
    fun provideLoadAllUsersUseCase(repository: UserRepository): LoadAllUsersUseCase {
        return LoadAllUsersUseCase(repository)
    }

    @Provides
    fun provideSearchUsersUseCase(loadAllUsersUseCase: LoadAllUsersUseCase): SearchUsersUseCase {
        return SearchUsersUseCase(loadAllUsersUseCase)
    }
}