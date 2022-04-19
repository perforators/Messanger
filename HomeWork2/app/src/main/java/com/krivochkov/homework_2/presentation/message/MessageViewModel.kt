package com.krivochkov.homework_2.presentation.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.use_cases.message.GetMessagesUseCase
import com.krivochkov.homework_2.domain.use_cases.message.SendMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.AddReactionUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.RemoveReactionUseCase
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.SingleEvent
import com.krivochkov.homework_2.presentation.message.adapters.message_adapter.items.DateSeparatorItem
import com.krivochkov.homework_2.presentation.message.adapters.message_adapter.items.MessageItem
import com.krivochkov.homework_2.presentation.pagination_component.MessagePaginationComponent
import com.krivochkov.homework_2.presentation.pagination_component.PaginationComponent
import com.krivochkov.homework_2.presentation.pagination_component.PaginationEvent
import com.krivochkov.homework_2.utils.convertToDate
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MessageViewModel(
    private val channelName: String,
    private val topicName: String,
    private val getMessagesUseCase: GetMessagesUseCase = GetMessagesUseCase(),
    private val addReactionUseCase: AddReactionUseCase = AddReactionUseCase(),
    private val removeReactionUseCase: RemoveReactionUseCase = RemoveReactionUseCase(),
    private val sendMessageUseCase: SendMessageUseCase = SendMessageUseCase()
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val paginationComponent: PaginationComponent<Message> by lazy {
        MessagePaginationComponent(
            source = { lastMessageId, numBefore ->
                getMessagesUseCase(channelName, topicName, lastMessageId, numBefore)
            },
            pageSize = MESSAGES_PAGE_SIZE
        )
    }

    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState>
        get() = _state

    private val _event: MutableLiveData<SingleEvent<UIEvent>> = MutableLiveData()
    val event: LiveData<SingleEvent<UIEvent>>
        get() = _event

    init {
        initPaginationObservers()

        getMessagesUseCase(channelName, topicName, 0, MESSAGES_PAGE_SIZE, true)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { _state.value = ScreenState.Loading }
            .subscribeBy(
                onSuccess = {
                    if (it.isNotEmpty()) submitMessages(it)
                    requestNextMessagePage()
                },
                onError = { requestNextMessagePage() }
            )
            .addTo(compositeDisposable)
    }

    fun requestNextMessagePage(isRetry: Boolean = false) {
        paginationComponent.requestNextPage(isRetry)
    }

    private fun refreshSingleMessage(messageId: Long) {
        paginationComponent.refreshElement { it.id == messageId }
    }

    private fun refreshNewestMessages() {
        paginationComponent.refreshFirstPage()
    }

    fun changeReaction(messageId: Long, emoji: Emoji) {
        val message = paginationComponent.elements.value?.find { it.id == messageId } ?: return
        val groupedReaction = message.groupedReactions.find { it.emoji.name == emoji.name }
        if (groupedReaction == null || !groupedReaction.isSelected) {
            addReaction(messageId, emoji)
        } else {
            removeReaction(messageId, emoji)
        }
    }

    fun addReaction(messageId: Long, emoji: Emoji) {
        addReactionUseCase(messageId, emoji.name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { refreshSingleMessage(messageId) },
                onError = { _event.value = SingleEvent(UIEvent.FailedAddReaction) }
            )
    }

    fun removeReaction(messageId: Long, emoji: Emoji) {
        removeReactionUseCase(messageId, emoji.name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { refreshSingleMessage(messageId) },
                onError = { _event.value = SingleEvent(UIEvent.FailedRemoveReaction) }
            )
    }

    fun sendMessage(message: String, attachedFiles: List<AttachedFile>) {
        sendMessageUseCase(channelName, topicName, message, attachedFiles)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { refreshNewestMessages() },
                onError = { _event.value = SingleEvent(UIEvent.FailedSendMessage) }
            )
    }

    private fun initPaginationObservers() {
        paginationComponent.elements.observeForever { if (it.isNotEmpty()) submitMessages(it) }

        paginationComponent.event.observeForever { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let { event ->
                when (event) {
                    is PaginationEvent.FailedLoadingNextPage -> _state.value = ScreenState.Error
                    is PaginationEvent.SuccessLoadingNextPage ->
                        _event.value = SingleEvent(UIEvent.HideLoadingNextMessagePage)
                    is PaginationEvent.StartLoadingNextPage ->
                        _event.value = SingleEvent(UIEvent.ShowLoadingNextMessagePage)
                    is PaginationEvent.FailedRefreshElements ->
                        _event.value = SingleEvent(UIEvent.FailedRefreshMessages)
                }
            }
        }
    }

    private fun submitMessages(messages: List<Message>) {
        Single.fromCallable { messages }
            .map { it.toMessageItemsWithDates() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { _state.value = ScreenState.MessagesLoaded(it) },
                onError = { _state.value = ScreenState.Error }
            )
    }

    private fun List<Message>.toMessageItemsWithDates(): List<Item> {
        val listItems = mutableListOf<Item>()
        val groupedMessages = groupBy { (it.date / SECONDS_IN_DAY) * SECONDS_IN_DAY }

        for (groups in groupedMessages) {
            val date = groups.key.convertToDate()
            val messageItems = groups.value.map { MessageItem(it) }
            listItems += DateSeparatorItem(date)
            listItems += messageItems
        }

        return listItems
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        paginationComponent.clear()
    }

    companion object {

        const val MESSAGES_PAGE_SIZE = 20
        private const val SECONDS_IN_DAY = 86400
    }
}