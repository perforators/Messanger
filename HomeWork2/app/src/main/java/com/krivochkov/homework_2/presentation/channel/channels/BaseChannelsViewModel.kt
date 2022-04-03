package com.krivochkov.homework_2.presentation.channel.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.SingleEvent
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem
import com.krivochkov.homework_2.utils.hasNotWhitespaces
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

abstract class BaseChannelsViewModel(
    private val loadTopicsUseCase: LoadTopicsUseCase = LoadTopicsUseCase()
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val searchingEvents: PublishSubject<String> = PublishSubject.create()
    private var lastQuery = ""

    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState>
        get() = _state

    private val _event: MutableLiveData<SingleEvent<UIEvent>> = MutableLiveData()
    val event: LiveData<SingleEvent<UIEvent>>
        get() = _event

    init {
        initSearchingEventsProcessing()
    }

    protected abstract fun searchChannels(query: String): Observable<Channel>

    fun loadChannels(query: String = "") {
        searchingEvents.onNext(query)
    }

    fun loadChannelsByLastQuery() {
        loadChannels(lastQuery)
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

    private fun initSearchingEventsProcessing() {
        searchingEvents
            .filter { it.isEmpty() || it.hasNotWhitespaces() } // при пустом запросе загружаются все каналы
            .map { it.trim() }
            .distinctUntilChanged { query, otherQuery ->
                // чтобы после ошибки обработать такой же запрос
                query == otherQuery && state.value !is ScreenState.Error
            }
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .switchMapSingle { query ->
               searchChannels(query)
                   .map { ChannelItem(it) }
                   .toList()
                   .observeOn(AndroidSchedulers.mainThread())
                   .map {
                       lastQuery = query
                       ScreenState.ChannelsLoaded(it) as ScreenState
                   }
                   .onErrorReturn {
                       lastQuery = query
                       ScreenState.Error
                   }
                   .subscribeOn(Schedulers.io())
                   .delaySubscription(3, TimeUnit.SECONDS)
                   .doOnSubscribe { _state.value = ScreenState.Loading }
                   .subscribeOn(AndroidSchedulers.mainThread())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { _state.value = it } )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}