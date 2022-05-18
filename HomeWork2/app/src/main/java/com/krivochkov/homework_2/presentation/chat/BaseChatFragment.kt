package com.krivochkov.homework_2.presentation.chat

import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.appComponent
import com.krivochkov.homework_2.di.chat.DaggerChatScreenComponent
import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.presentation.chat.adapters.attached_file_adapter.AttachedFileAdapter
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.ChatAdapter
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.PaginationAdapterHelper
import com.krivochkov.homework_2.presentation.chat.elm.ChatEffect
import com.krivochkov.homework_2.presentation.chat.elm.ChatEvent
import com.krivochkov.homework_2.presentation.chat.elm.ChatState
import com.krivochkov.homework_2.presentation.chat.emoji_pick.EmojiPickFragment
import com.krivochkov.homework_2.presentation.custom_views.ErrorView
import com.krivochkov.homework_2.utils.createFileAndGetPathWithFileNameFromCache
import vivid.money.elmslie.android.base.ElmFragment
import javax.inject.Inject

abstract class BaseChatFragment(@LayoutRes contentLayoutId: Int) :
    EmojiPickFragment.OnEmojiPickListener,
    ElmFragment<ChatEvent, ChatEffect, ChatState>(contentLayoutId) {

    @Inject
    internal lateinit var chatViewModelFactory: ChatViewModelFactory

    protected val chatViewModel: ChatViewModel by viewModels { chatViewModelFactory }
    private val fileSharedViewModel: FilePickSharedViewModel by activityViewModels()
    private val topicSharedViewModel: TopicPickSharedViewModel by activityViewModels()

    protected lateinit var chatAdapter: ChatAdapter
    protected lateinit var attachedFileAdapter: AttachedFileAdapter

    override fun createStore() = chatViewModel.chatStore

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerChatScreenComponent.factory()
            .create(appComponent())
            .inject(this)
    }

    override fun handleEffect(effect: ChatEffect) {
        when (effect) {
            is ChatEffect.CachedMessagesLoadError -> {
                showToast(R.string.failed_load_cached_messages)
            }
            is ChatEffect.NextPageLoadError -> {
                showToast(R.string.failed_load_next_page)
            }
            is ChatEffect.SendMessageError -> {
                showToast(R.string.failed_send_message)
            }
            is ChatEffect.AddReactionError -> {
                showToast(R.string.failed_add_reaction)
            }
            is ChatEffect.RemoveReactionError -> {
                showToast(R.string.failed_remove_reaction)
            }
            is ChatEffect.RefreshSingleMessageError -> {
                showToast(R.string.failed_refresh_message)
            }
            is ChatEffect.ShowEmojiPicker -> {
                showEmojiPick(effect.messageId)
            }
            is ChatEffect.ShowFilePicker -> {
                fileSharedViewModel.pickFile()
            }
            is ChatEffect.NavigateUp -> {
                findNavController().navigateUp()
            }
            else -> { Log.d(TAG, "Неизвестный эффект : $effect") }
        }
    }

    override fun onResume() {
        super.onResume()
        fileSharedViewModel.fileUri.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { uri ->
                val (path, fileName) =
                    createFileAndGetPathWithFileNameFromCache(requireContext(), uri)
                val type = requireContext().contentResolver.getType(uri) ?: return@let
                store.accept(ChatEvent.Ui.AddAttachedFile(AttachedFile(fileName, type, path)))
            }
        }

        topicSharedViewModel.topic.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { topic ->
                store.accept(ChatEvent.Ui.ChangeTopicsForSendingMessages(topic))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fileSharedViewModel.fileUri.removeObservers(viewLifecycleOwner)
        topicSharedViewModel.topic.removeObservers(viewLifecycleOwner)
    }

    protected fun initToolbar(
        toolbar: MaterialToolbar,
        backButton: ImageButton,
        channelName: String
    ) {
        toolbar.title = channelName
        backButton.setOnClickListener {
            store.accept(ChatEvent.Ui.BackButtonClick)
        }
    }

    protected fun initErrorView(errorView: ErrorView) {
        errorView.apply {
            setOnErrorButtonClickListener {
                store.accept(ChatEvent.Ui.RefreshFirstPage)
            }
            text = requireContext().getString(R.string.error_text)
        }
    }

    protected fun initMessageRecycler(recyclerView: RecyclerView) {
        initMessageAdapter(recyclerView)
        recyclerView.adapter = chatAdapter

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
    }

    private fun initMessageAdapter(recyclerView: RecyclerView) {
        chatAdapter = ChatAdapter(
            PaginationAdapterHelper { store.accept(ChatEvent.Ui.LoadNextPage) }
        ).apply {
            setOnAddMyReactionListener { messageId, emoji ->
                store.accept(ChatEvent.Ui.AddReaction(messageId, emoji))
            }
            setOnRemoveMyReactionListener { messageId, emoji ->
                store.accept(ChatEvent.Ui.RemoveReaction(messageId, emoji))
            }
            setOnChoosingReactionListener { messageId ->
                store.accept(ChatEvent.Ui.ShowEmojiPicker(messageId))
            }
            setOnHeaderTopicClickListener { topic ->
                store.accept(ChatEvent.Ui.ShowTopicChatContent(topic))
            }
        }

        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart >= chatAdapter.itemCount - 1) {
                    recyclerView.smoothScrollToPosition(chatAdapter.itemCount)
                }
            }
        })
    }

    protected fun initInputFileRecycler(recyclerView: RecyclerView) {
        attachedFileAdapter = AttachedFileAdapter {
            store.accept(ChatEvent.Ui.RemoveAttachedFile(it))
        }
        recyclerView.adapter = attachedFileAdapter
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    protected fun initInputField(
        inputField: EditText,
        sendButton: ImageButton,
        addFileButton: ImageButton
    ) {
        sendButton.setOnClickListener {
            val inputText = inputField.text.toString()

            if (inputText.isNotEmpty()) {
                inputField.setText("")
                store.accept(
                    ChatEvent.Ui.SendMessage(inputText)
                )
            }
        }

        addFileButton.setOnClickListener {
            store.accept(ChatEvent.Ui.ShowFilePicker)
        }

        inputField.doOnTextChanged { _, _, _, _ ->
            sendButton.isVisible = inputField.text.isNotEmpty()
            addFileButton.isVisible = inputField.text.isEmpty()
        }
    }

    protected fun initTopicForSendingMessagesField(field: TextView, channelId: Long) {
        field.setOnClickListener {
            store.accept(ChatEvent.Ui.ShowTopicPicker(channelId))
        }
    }

    private fun showEmojiPick(messageId: Long) {
        EmojiPickFragment.newInstance(messageId)
            .show(childFragmentManager, EMOJI_PICK_FRAGMENT_TAG)
    }

    private fun showToast(@StringRes stringResId: Int) {
        val text = requireContext().getString(stringResId)
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    override fun onEmojiPick(messageId: Long, emoji: Emoji) {
        store.accept(ChatEvent.Ui.UpdateReaction(messageId, emoji))
    }

    companion object {

        private const val TAG = "BaseChatFragment"
        private const val EMOJI_PICK_FRAGMENT_TAG = "TAG_EMOJI_PICK"
    }
}