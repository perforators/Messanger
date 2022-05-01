package com.krivochkov.homework_2.di.people

import com.krivochkov.homework_2.domain.repositories.UserRepository
import com.krivochkov.homework_2.presentation.SearchQueryFilter

interface PeopleScreenDependencies {

    fun getUserRepository(): UserRepository

    fun getSearchQueryFilter(): SearchQueryFilter
}