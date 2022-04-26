package com.krivochkov.homework_2.di.people.modules

import com.krivochkov.homework_2.di.people.annotations.PeopleScreenScope
import com.krivochkov.homework_2.domain.use_cases.user.SearchUsersUseCase
import com.krivochkov.homework_2.presentation.people.elm.PeopleActor
import com.krivochkov.homework_2.presentation.people.elm.PeopleStoreFactory
import dagger.Module
import dagger.Provides

@Module
class PeopleElmModule {

    @Provides
    fun providePeopleActor(searchUsersUseCase: SearchUsersUseCase): PeopleActor {
        return PeopleActor(searchUsersUseCase)
    }

    @Provides
    @PeopleScreenScope
    fun providePeopleStoreFactory(actor: PeopleActor): PeopleStoreFactory {
        return PeopleStoreFactory(actor)
    }
}