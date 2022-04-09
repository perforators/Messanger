package com.krivochkov.homework_2.presentation.channel.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.use_cases.SearchableUseCase
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.SingleEvent
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem
import com.krivochkov.homework_2.presentation.search_component.SearchComponent
import com.krivochkov.homework_2.presentation.search_component.SearchComponentImpl
import com.krivochkov.homework_2.presentation.search_component.SearchStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

abstract class BaseChannelsViewModel(
    private val searchableUseCase: SearchableUseCase<Channel>,
    private val loadTopicsUseCase: LoadTopicsUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val searchComponent: SearchComponent<Channel> by lazy {
        SearchComponentImpl(searchableUseCase) { channel, query ->
            channel.name.contains(query)
        }
    }

    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState>
        get() = _state

    private val _event: MutableLiveData<SingleEvent<UIEvent>> = MutableLiveData()
    val event: LiveData<SingleEvent<UIEvent>>
        get() = _event

    init {
        searchComponent.searchStatus.observeForever { searchStatus ->
            when (searchStatus) {
                is SearchStatus.Success -> {
                    Single.fromCallable { searchStatus.data }
                        .map { it.map { channel -> ChannelItem(channel) } }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onSuccess = { _state.value = ScreenState.ChannelsLoaded(it) },
                            onError = { _state.value = ScreenState.Error }
                        )
                }
                is SearchStatus.Error -> _state.value = ScreenState.Error
                is SearchStatus.Searching -> _state.value = ScreenState.Loading
            }
        }

        loadChannels()
    }

    fun loadChannels(query: String = "") {
        searchComponent.search(query)
    }

    fun loadChannelsByLastQuery() {
        searchComponent.searchByLastQuery()
    }

    fun loadTopicsInChannel(channelId: Long) {
        loadTopicsUseCase(channelId)
            .flatMapObservable { Observable.fromIterable(it) }
            .map { TopicItem(it, channelId) }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    _event.value = SingleEvent(UIEvent.SubmitTopicsInChannel(channelId, it))
                },
                onError = {
                    _event.value = SingleEvent(UIEvent.FailedLoadTopics)
                }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        searchComponent.clearSearch()
    }
}