package com.krivochkov.homework_2.presentation.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krivochkov.homework_2.presentation.SearchQueryFilter
import com.krivochkov.homework_2.presentation.people.elm.PeopleStoreFactory
import javax.inject.Inject

class PeopleViewModelFactory @Inject constructor(
    private val peopleStoreFactory: PeopleStoreFactory,
    private val searchQueryFilter: SearchQueryFilter
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PeopleViewModel(peopleStoreFactory, searchQueryFilter) as T
    }
}