package com.krivochkov.homework_2.presentation.search_component

import androidx.lifecycle.LiveData

interface SearchComponent<T> {

    val searchStatus: LiveData<SearchStatus<T>>

    fun search(searchQuery: SearchQuery)

    fun searchByLastQuery()

    fun clearSearch()
}