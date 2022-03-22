package com.krivochkov.homework_2.presentation.channel.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.SingleEvent

abstract class BaseChannelsViewModel(
    private val loadTopicsUseCase: LoadTopicsUseCase = LoadTopicsUseCase()
) : ViewModel() {

    protected val _channels: MutableLiveData<List<Channel>> = MutableLiveData()
    val channels: LiveData<List<Channel>>
        get() = _channels

    private val _topicsInChannel: MutableLiveData<SingleEvent<Pair<Long, List<Topic>>>> = MutableLiveData()
    val topicsInChannel: LiveData<SingleEvent<Pair<Long, List<Topic>>>>
        get() = _topicsInChannel

    abstract fun loadChannels()

    fun loadTopicsInChannel(channelId: Long) {
        _topicsInChannel.value = SingleEvent(loadTopicsUseCase(channelId))
    }
}