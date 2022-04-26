package com.krivochkov.homework_2.di.people.modules

import com.krivochkov.homework_2.presentation.SearchQueryFilter
import com.krivochkov.homework_2.presentation.people.PeopleViewModelFactory
import com.krivochkov.homework_2.presentation.people.elm.PeopleStoreFactory
import dagger.Module
import dagger.Provides

@Module
class PeopleViewModelModule {

    @Provides
    fun providePeopleViewModelFactory(
        peopleStoreFactory: PeopleStoreFactory,
        searchQueryFilter: SearchQueryFilter
    ): PeopleViewModelFactory {
        return PeopleViewModelFactory(peopleStoreFactory, searchQueryFilter)
    }
}