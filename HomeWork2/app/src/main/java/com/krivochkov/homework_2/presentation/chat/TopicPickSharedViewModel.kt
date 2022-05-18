package com.krivochkov.homework_2.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.SingleEvent

class TopicPickSharedViewModel : ViewModel() {

    private val _topic: MutableLiveData<SingleEvent<Topic>> = MutableLiveData()
    val topic: LiveData<SingleEvent<Topic>>
        get() = _topic

    fun sendTopic(topic: Topic) {
        _topic.value = SingleEvent(topic)
    }
}