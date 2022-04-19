package com.krivochkov.homework_2.presentation.channel.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.use_cases.channel.LoadChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.SingleEvent
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem
import com.krivochkov.homework_2.presentation.search_component.SearchComponent
import com.krivochkov.homework_2.presentation.search_component.SearchComponentImpl
import com.krivochkov.homework_2.presentation.search_component.SearchQuery
import com.krivochkov.homework_2.presentation.search_component.SearchStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

abstract class BaseChannelsViewModel(
    private val loadTopicsUseCase: LoadTopicsUseCase,
    private val loadChannelsUseCase: LoadChannelsUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val searchComponent: SearchComponent<Channel> by lazy {
        SearchComponentImpl(
            source = { loadChannelsUseCase.load(false) },
            filter = { channel, query -> channel.name.contains(query) }
        )
    }

    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState>
        get() = _state

    private val _event: MutableLiveData<SingleEvent<UIEvent>> = MutableLiveData()
    val event: LiveData<SingleEvent<UIEvent>>
        get() = _event

    init {
        initSearchStatusObserver()

        loadChannelsUseCase.load(true)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { _state.value = ScreenState.Loading }
            .subscribeBy(
                onSuccess = {
                    if (it.isEmpty()) {
                        searchChannels()
                    } else {
                        submitChannels(it)
                        searchChannels(ignoreSearchingStatus = true)
                    }
                },
                onError = { searchChannels() }
            )
            .addTo(compositeDisposable)
    }

    fun searchChannels(query: String = "", ignoreSearchingStatus: Boolean = false) {
        searchComponent.search(SearchQuery(query, ignoreSearchingStatus))
    }

    fun searchChannelsByLastQuery() {
        searchComponent.searchByLastQuery()
    }

    fun loadTopics(channelId: Long) {
        loadTopicsUseCase(channelId, true)
            .map { it.map { topic -> TopicItem(topic, channelId) } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    if (it.isNotEmpty()) _event.value =
                        SingleEvent(UIEvent.SubmitTopicsInChannel(channelId, it))
                    loadActualTopics(channelId)
                },
                onError = { loadActualTopics(channelId) }
            )
            .addTo(compositeDisposable)
    }

    private fun loadActualTopics(channelId: Long) {
        loadTopicsUseCase(channelId, false)
            .map { it.map { topic -> TopicItem(topic, channelId) } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { _event.value = SingleEvent(UIEvent.SubmitTopicsInChannel(channelId, it)) },
                onError = { _event.value = SingleEvent(UIEvent.FailedLoadTopics) }
            )
            .addTo(compositeDisposable)
    }

    private fun initSearchStatusObserver() {
        searchComponent.searchStatus.observeForever { searchStatus ->
            when (searchStatus) {
                is SearchStatus.Success -> submitChannels(searchStatus.data)
                is SearchStatus.Error -> _state.value = ScreenState.Error
                is SearchStatus.Searching -> _state.value = ScreenState.Loading
            }
        }
    }

    private fun submitChannels(channels: List<Channel>) {
        Single.fromCallable { channels }
            .map { it.map { channel -> ChannelItem(channel) } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { _state.value = ScreenState.ChannelsLoaded(it) },
                onError = { _state.value = ScreenState.Error }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        searchComponent.clearSearch()
    }
}