package com.krivochkov.homework_2.presentation.channel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.SearchQueryQueue
import com.krivochkov.homework_2.presentation.SingleEvent

class SharedViewModel : ViewModel() {

    private val searchQueryQueue = SearchQueryQueue()

    private val _selectedTopic: MutableLiveData<SingleEvent<Pair<Channel, Topic>>> = MutableLiveData()
    val selectedTopic: LiveData<SingleEvent<Pair<Channel, Topic>>>
        get() = _selectedTopic

    private val _searchQuery: MutableLiveData<SingleEvent<String>> = MutableLiveData()
    val searchQuery: LiveData<SingleEvent<String>>
        get() = _searchQuery

    init {
        searchQueryQueue.observeOutputQueries {
            _searchQuery.value = SingleEvent(it)
        }
    }

    fun selectTopic(channel: Channel, topic: Topic) {
        _selectedTopic.value = SingleEvent(channel to topic)
    }

    fun sendSearchQuery(query: String) {
        searchQueryQueue.sendQuery(query)
    }

    override fun onCleared() {
        super.onCleared()
        searchQueryQueue.dispose()
    }
}