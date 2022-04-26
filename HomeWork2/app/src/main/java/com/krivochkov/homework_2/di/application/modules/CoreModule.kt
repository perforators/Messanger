package com.krivochkov.homework_2.di.application.modules

import com.krivochkov.homework_2.presentation.SearchQueryFilter
import dagger.Module
import dagger.Provides

@Module
class CoreModule {

    @Provides
    fun provideSearchQueryFilter(): SearchQueryFilter {
        return SearchQueryFilter()
    }
}