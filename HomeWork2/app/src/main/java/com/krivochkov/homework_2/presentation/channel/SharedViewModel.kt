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

    fun selectTopic(channel: Channel, topic: Topic) {
        _selectedTopic.value = SingleEvent(channel to topic)
    }
}