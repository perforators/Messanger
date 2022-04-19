package com.krivochkov.homework_2.presentation.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.presentation.SearchQueryQueue
import com.krivochkov.homework_2.presentation.SingleEvent

class PeopleViewModel : ViewModel() {

    private val searchQueryQueue = SearchQueryQueue()

    private val _searchQuery: MutableLiveData<SingleEvent<String>> = MutableLiveData()
    val searchQuery: LiveData<SingleEvent<String>>
        get() = _searchQuery

    init {
        searchQueryQueue.observeOutputQueries {
            _searchQuery.value = SingleEvent(it)
        }
    }

    fun addQueryToQueue(query: String) {
        searchQueryQueue.sendQuery(query)
    }
}