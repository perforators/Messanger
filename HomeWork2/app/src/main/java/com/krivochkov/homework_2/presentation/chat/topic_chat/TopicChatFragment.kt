package com.krivochkov.homework_2.presentation.chat.topic_chat

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentTopicChatBinding
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.LoadingItem
import com.krivochkov.homework_2.presentation.chat.BaseChatFragment
import com.krivochkov.homework_2.presentation.chat.elm.ChatEvent
import com.krivochkov.homework_2.presentation.chat.elm.ChatState
import com.krivochkov.homework_2.presentation.chat.mappers.toMessageItemsWithDates

class TopicChatFragment : BaseChatFragment(R.layout.fragment_topic_chat) {

    private val args by navArgs<TopicChatFragmentArgs>()

    private val channelName: String
        get() = args.channel.name

    private val topicName: String
        get() = args.topic.name

    private val viewBinding: FragmentTopicChatBinding by viewBinding()

    override val initEvent: ChatEvent
        get() = ChatEvent.Ui.Init(channelName, topicName)

    override fun render(state: ChatState) {
        viewBinding.apply {
            contentLayout.loading.loadingLayout.apply {
                isVisible = state.isLoading
                if (state.isLoading) startShimmer() else stopShimmer()
            }

            val visibleContentCondition = state.isLoading.not() && state.error == null
            contentLayout.apply {
                recyclerMessages.isVisible = visibleContentCondition
                attachedFiles.recyclerInputFiles.isVisible = visibleContentCondition
                inputField.isVisible = visibleContentCondition
                buttonBox.buttonBoxLayout.isVisible = visibleContentCondition
            }

            attachedFileAdapter.files = state.attachedFiles

            contentLayout.error.isVisible = state.error != null
        }
    }

    override fun mapList(state: ChatState): List<Item> {
        val itemsList = state.items.toMutableList()
        return if (itemsList.contains(LoadingItem)) {
            listOf(LoadingItem) + itemsList.filterIsInstance<Message>().toMessageItemsWithDates()
        } else {
            itemsList.filterIsInstance<Message>().toMessageItemsWithDates()
        }
    }

    override fun renderList(state: ChatState, list: List<Any>) {
        chatAdapter.items = list.filterIsInstance<Item>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.apply {
            topicText.text = String.format(
                getString(R.string.topic_text),
                topicName
            )
            toolbarLayout.apply {
                initToolbar(toolbar, backButton, channelName)
            }
            initErrorView(contentLayout.error)
            initMessageRecycler(contentLayout.recyclerMessages)
            initInputFileRecycler(attachedFiles.recyclerInputFiles)
            buttonBox.apply {
                initInputField(inputField, sendButton, addFileButton)
            }
        }
    }
}