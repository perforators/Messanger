package com.krivochkov.homework_2.presentation.channel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.SingleEvent

class SharedViewModel : ViewModel() {

    private val _selectedTopic: MutableLiveData<SingleEvent<Pair<Channel, Topic>>> = MutableLiveData()
    val selectedTopic: LiveData<SingleEvent<Pair<Channel, Topic>>>
        get() = _selectedTopic

    private val _selectedChannel: MutableLiveData<SingleEvent<Channel>> = MutableLiveData()
    val selectedChannel: LiveData<SingleEvent<Channel>>
        get() = _selectedChannel

    private val _showCreateChannelScreenEvent: MutableLiveData<SingleEvent<Any>> = MutableLiveData()
    val showCreateChannelScreenEvent: LiveData<SingleEvent<Any>>
        get() = _showCreateChannelScreenEvent

    private val _searchQuery: MutableLiveData<SingleEvent<String>> = MutableLiveData()
    val searchQuery: LiveData<SingleEvent<String>>
        get() = _searchQuery

    fun selectTopic(channel: Channel, topic: Topic) {
        _selectedTopic.value = SingleEvent(channel to topic)
    }

    fun selectChannel(channel: Channel) {
        _selectedChannel.value = SingleEvent(channel)
    }

    fun showCreateChannelScreen() {
        _showCreateChannelScreenEvent.value = SingleEvent(Any())
    }

    fun sendSearchQuery(query: String) {
        _searchQuery.value = SingleEvent(query)
    }
}