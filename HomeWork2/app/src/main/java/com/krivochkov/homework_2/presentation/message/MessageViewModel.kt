package com.krivochkov.homework_2.presentation.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.use_cases.message.GetMessagesUseCase
import com.krivochkov.homework_2.domain.use_cases.message.SendMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.AddReactionUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.RemoveReactionUseCase
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.SingleEvent
import com.krivochkov.homework_2.presentation.message.adapter.items.DateSeparatorItem
import com.krivochkov.homework_2.presentation.message.adapter.items.MessageItem
import com.krivochkov.homework_2.utils.convertToDate
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

    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState>
        get() = _state

    private val _event: MutableLiveData<SingleEvent<UIEvent>> = MutableLiveData()
    val event: LiveData<SingleEvent<UIEvent>>
        get() = _event

    init {
        refreshMessages()
    }

    fun refreshMessages(withLoadingAnim: Boolean = true) {
        getMessagesUseCase(channelName, topicName)
            .map { it.toMessageItemsWithDates() }
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { if (withLoadingAnim) _state.value = ScreenState.Loading }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _state.value = ScreenState.MessagesLoaded(it)
                },
                onError = {
                    _state.value = ScreenState.Error
                }
            )
            .addTo(compositeDisposable)
    }

    fun addReaction(messageId: Long, emoji: Emoji) {
        addReactionUseCase(messageId, emoji.name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    refreshMessages(false)
                },
                onError = {
                    _event.value = SingleEvent(UIEvent.FailedAddReaction)
                }
            )
    }

    fun removeReaction(messageId: Long, emoji: Emoji) {
        removeReactionUseCase(messageId, emoji.name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    refreshMessages(false)
                },
                onError = {
                    _event.value = SingleEvent(UIEvent.FailedRemoveReaction)
                }
            )
    }

    fun sendMessage(message: String) {
        sendMessageUseCase(channelName, topicName, message)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    refreshMessages(false)
                },
                onError = {
                    _event.value = SingleEvent(UIEvent.FailedSendMessage)
                }
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
    }

    companion object {

        private const val SECONDS_IN_DAY = 86400
    }
}