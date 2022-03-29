package com.krivochkov.homework_2.presentation.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.data.repositories.MessageRepositoryImpl
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.use_cases.message.GetAllMessagesUseCase
import com.krivochkov.homework_2.domain.use_cases.message.SendMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.UpdateReactionUseCase
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
import java.util.concurrent.TimeUnit

class MessageViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val getAllMessagesUseCase: GetAllMessagesUseCase
    private val updateReactionUseCase: UpdateReactionUseCase
    private val sendMessageUseCase: SendMessageUseCase

    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState>
        get() = _state

    private val _event: MutableLiveData<SingleEvent<UIEvent>> = MutableLiveData()
    val event: LiveData<SingleEvent<UIEvent>>
        get() = _event

    init {
        val repository = MessageRepositoryImpl()
        getAllMessagesUseCase = GetAllMessagesUseCase(repository)
        updateReactionUseCase = UpdateReactionUseCase(repository)
        sendMessageUseCase = SendMessageUseCase(repository)

        refreshMessages(true, 3)
    }

    fun refreshMessages(withLoadingAnim: Boolean, delay: Long = 0) {
        getAllMessagesUseCase()
            .map { it.toMessageItemsWithDates() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .delaySubscription(delay, TimeUnit.SECONDS)
            .doOnSubscribe { if (withLoadingAnim) _state.value = ScreenState.Loading }
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

    fun updateReaction(messageId: Long, emoji: String) {
        updateReactionUseCase(messageId, emoji)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    refreshMessages(false)
                },
                onError = {
                    _event.value = SingleEvent(UIEvent.FailedUpdateReaction)
                }
            )
    }

    fun sendMessage(message: String) {
        sendMessageUseCase(message)
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
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

    companion object {
        private const val SECONDS_IN_DAY = 86400
    }
}