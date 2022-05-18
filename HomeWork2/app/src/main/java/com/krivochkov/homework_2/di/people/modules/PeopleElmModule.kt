package com.krivochkov.homework_2.di.people.modules

import com.krivochkov.homework_2.domain.use_cases.user.LoadAllUsersUseCase
import com.krivochkov.homework_2.domain.use_cases.user.SearchUsersUseCase
import com.krivochkov.homework_2.presentation.people.elm.PeopleActor
import dagger.Module
import dagger.Provides

@Module
class PeopleElmModule {

    @Provides
    fun providePeopleActor(
        searchUsersUseCase: SearchUsersUseCase,
        loadAllUsersUseCase: LoadAllUsersUseCase
    ): PeopleActor {
        return PeopleActor(searchUsersUseCase, loadAllUsersUseCase)
    }
}