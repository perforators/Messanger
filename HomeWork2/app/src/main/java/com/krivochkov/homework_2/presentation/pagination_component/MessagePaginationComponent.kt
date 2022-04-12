package com.krivochkov.homework_2.presentation.pagination_component

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.presentation.SingleEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MessagePaginationComponent(
    private val source: (lastMessageId: Long, numBefore: Int) -> Single<List<Message>>,
    private val pageSize: Int
) : PaginationComponent<Message> {

    private val compositeDisposable = CompositeDisposable()
    private val requestEvents: PublishSubject<MessagePage> = PublishSubject.create()

    private val _elements: MutableLiveData<List<Message>> = MutableLiveData(listOf())
    override val elements: LiveData<List<Message>>
        get() = _elements

    private val _event: MutableLiveData<SingleEvent<PaginationEvent>> = MutableLiveData()
    override val event: LiveData<SingleEvent<PaginationEvent>>
        get() = _event

    private val messages: List<Message>
        get() = _elements.value!!

    init {
        initRequestEventsProcessing()
    }

    override fun requestNextPage(isRetry: Boolean) {
        val lastMessageId = if (messages.isEmpty()) 0 else messages[0].id - 1
        requestEvents.onNext(MessagePage(lastMessageId, pageSize, isRetry))
    }

    override fun refreshFirstPage() {
        source(0, pageSize)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { newMessages ->
                    changeMessages { oldMessages ->
                        if (newMessages.isEmpty()) return@changeMessages oldMessages
                        if (oldMessages.isEmpty()) return@changeMessages newMessages

                        val indexOfOccurrence = oldMessages.indexOfFirst {
                            it.id == newMessages[0].id
                        }

                        if (indexOfOccurrence == -1) {
                            oldMessages.apply { addAll(0, newMessages) }
                        } else {
                            val updatedMessages = oldMessages.subList(0, indexOfOccurrence)
                            updatedMessages.apply { addAll(indexOfOccurrence, newMessages) }
                        }
                    }
                },
                onError = { _event.value = SingleEvent(PaginationEvent.FailedRefreshElements) }
            )
    }

    override fun refreshElement(selector: (Message) -> Boolean) {
        val message = messages.find(selector) ?: return
        source(message.id, 0)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { updatedMessage ->
                    changeMessages { oldMessages ->
                        val index = oldMessages.indexOfFirst { it.id == updatedMessage[0].id }
                        if (index != -1) {
                            oldMessages.apply { this[index] = updatedMessage[0] }
                        } else {
                            oldMessages
                        }
                    }
                },
                onError = { _event.value = SingleEvent(PaginationEvent.FailedRefreshElements) }
            )
    }

    override fun clear() {
        compositeDisposable.clear()
    }

    private fun initRequestEventsProcessing() {
        requestEvents
            .distinctUntilChanged { prev, current -> prev == current && !current.isRetry }
            .observeOn(Schedulers.io())
            .concatMapSingle { page ->
                source(page.lastMessageId, page.numBefore)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { _event.value = SingleEvent(PaginationEvent.StartLoadingNextPage) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { _event.value = SingleEvent(PaginationEvent.FailedLoadingNextPage) }
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn { listOf() }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isNotEmpty()) {
                        changeMessages { messages -> messages.apply { addAll(0, it) } }
                    }
                    _event.value = SingleEvent(PaginationEvent.SuccessLoadingNextPage)
                },
                onError = { _event.value = SingleEvent(PaginationEvent.FailedLoadingNextPage) }
            )
    }

    private fun changeMessages(action: (MutableList<Message>) -> List<Message>) {
        _elements.value = action(messages.toMutableList())
    }
}