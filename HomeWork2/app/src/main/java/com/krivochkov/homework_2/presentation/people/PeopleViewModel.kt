package com.krivochkov.homework_2.presentation.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.presentation.SearchQueryFilter
import com.krivochkov.homework_2.presentation.SingleEvent

class PeopleViewModel : ViewModel() {

    private val searchQueryFilter = SearchQueryFilter()

    private val _searchQuery: MutableLiveData<SingleEvent<String>> = MutableLiveData()
    val searchQuery: LiveData<SingleEvent<String>>
        get() = _searchQuery

    init {
        searchQueryFilter.observeFilteredQueries {
            _searchQuery.value = SingleEvent(it)
        }
    }

    fun addQueryToQueue(query: String) {
        searchQueryFilter.sendQuery(query)
    }
}