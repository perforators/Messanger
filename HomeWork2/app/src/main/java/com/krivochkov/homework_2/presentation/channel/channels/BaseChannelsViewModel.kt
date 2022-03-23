package com.krivochkov.homework_2.presentation.channel.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

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

    private val jobs = WeakHashMap<Long, Job>() // для теста, чтобы одинаковые запросы не выполнялись

    fun loadTopicsInChannel(channelId: Long) {
        jobs[channelId]?.cancel()
        jobs[channelId] = viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            _topicsInChannel.postValue(SingleEvent(loadTopicsUseCase(channelId)))
        }
    }
}