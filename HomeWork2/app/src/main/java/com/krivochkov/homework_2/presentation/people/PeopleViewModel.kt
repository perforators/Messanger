package com.krivochkov.homework_2.presentation.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.presentation.SearchQueryQueue

class PeopleViewModel : ViewModel() {

    private val searchQueryQueue = SearchQueryQueue()

    private val _searchQuery: MutableLiveData<String> = MutableLiveData()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    init {
        searchQueryQueue.observeOutputQueries {
            _searchQuery.value = it
        }
    }

    fun addQueryToQueue(query: String) {
        searchQueryQueue.sendQuery(query)
    }
}