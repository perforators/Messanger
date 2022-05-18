package com.krivochkov.homework_2.presentation.chat.channel_chat

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentChannelChatBinding
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.LoadingItem
import com.krivochkov.homework_2.presentation.chat.BaseChatFragment
import com.krivochkov.homework_2.presentation.chat.elm.ChatEffect
import com.krivochkov.homework_2.presentation.chat.elm.ChatEvent
import com.krivochkov.homework_2.presentation.chat.elm.ChatState
import com.krivochkov.homework_2.presentation.chat.mappers.toMessageItemsWithDatesAndTopics

class ChannelChatFragment : BaseChatFragment(R.layout.fragment_channel_chat) {

    private val args by navArgs<ChannelChatFragmentArgs>()

    private val channelName: String
        get() = args.channel.name

    private val viewBinding: FragmentChannelChatBinding by viewBinding()

    override val initEvent: ChatEvent
        get() = ChatEvent.Ui.Init(channelName)

    override fun createStore() = chatViewModel.chatStore

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
                topicForSendingMessages.isVisible = visibleContentCondition
            }

            attachedFileAdapter.files = state.attachedFiles

            contentLayout.error.isVisible = state.error != null
            topicForSendingMessages.text = state.topicForSendingMessages
        }
    }

    override fun handleEffect(effect: ChatEffect) {
        when (effect) {
            is ChatEffect.ShowTopicChatContent -> showTopicChatContent(effect.topic)
            is ChatEffect.ShowTopicPicker -> showTopicPicker()
            else -> super.handleEffect(effect)
        }
    }

    override fun mapList(state: ChatState): List<Item> {
        val itemsList = state.items.toMutableList()
        return if (itemsList.contains(LoadingItem)) {
            listOf(LoadingItem) +
                    itemsList.filterIsInstance<Message>().toMessageItemsWithDatesAndTopics()
        } else {
            itemsList.filterIsInstance<Message>().toMessageItemsWithDatesAndTopics()
        }
    }

    override fun renderList(state: ChatState, list: List<Any>) {
        chatAdapter.items = list.filterIsInstance<Item>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.apply {
            toolbarLayout.apply {
                initToolbar(toolbar, backButton, channelName)
            }
            initErrorView(contentLayout.error)
            initMessageRecycler(contentLayout.recyclerMessages)
            initInputFileRecycler(attachedFiles.recyclerInputFiles)
            buttonBox.apply {
                initInputField(inputField, sendButton, addFileButton)
            }
            initTopicForSendingMessagesField(topicForSendingMessages, args.channel.id)
        }
    }

    private fun showTopicChatContent(topic: Topic) {
        findNavController().navigate(
            ChannelChatFragmentDirections.actionNavigationChannelChatToNavigationTopicChat(
                args.channel,
                topic
            )
        )
    }

    private fun showTopicPicker() {
        findNavController().navigate(
            ChannelChatFragmentDirections.actionNavigationChannelChatToTopicPickFragment(
                args.channel.id
            )
        )
    }
}