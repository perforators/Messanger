package com.krivochkov.homework_2.presentation.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.appComponent
import com.krivochkov.homework_2.databinding.FragmentChatBinding
import com.krivochkov.homework_2.di.chat.DaggerChatScreenComponent
import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.LoadingItem
import com.krivochkov.homework_2.presentation.chat.adapters.attached_file_adapter.AttachedFileAdapter
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.ChatAdapter
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.PaginationAdapterHelper
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.DateSeparatorItem
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.MessageItem
import com.krivochkov.homework_2.presentation.chat.elm.ChatEffect
import com.krivochkov.homework_2.presentation.chat.elm.ChatEvent
import com.krivochkov.homework_2.presentation.chat.elm.ChatState
import com.krivochkov.homework_2.presentation.chat.emoji_pick.EmojiPickFragment
import com.krivochkov.homework_2.utils.convertToDate
import com.krivochkov.homework_2.utils.createFileAndGetPathWithFileNameFromCache
import vivid.money.elmslie.android.base.ElmFragment
import javax.inject.Inject

class ChatFragment :
    EmojiPickFragment.OnEmojiPickListener, ElmFragment<ChatEvent, ChatEffect, ChatState>() {

    @Inject
    internal lateinit var chatViewModelFactory: ChatViewModelFactory

    private val args by navArgs<ChatFragmentArgs>()

    private val channelName: String
        get() = args.channel.name

    private val topicName: String
        get() = args.topic.name

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var attachedFileAdapter: AttachedFileAdapter

    private val sharedViewModel: FilePickerSharedViewModel by activityViewModels()
    private val chatViewModel: ChatViewModel by viewModels { chatViewModelFactory }

    override val initEvent: ChatEvent
        get() = ChatEvent.Ui.Init(channelName, topicName)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerChatScreenComponent.factory()
            .create(appComponent())
            .inject(this)
    }

    override fun createStore() = chatViewModel.chatStore

    override fun render(state: ChatState) {
        binding.apply {
            loading.loadingLayout.apply {
                isVisible = state.isLoading
                if (state.isLoading) startShimmer() else stopShimmer()
            }

            val visibleContentCondition = state.isLoading.not() && state.error == null
            recyclerMessages.isVisible = visibleContentCondition
            recyclerInputFiles.isVisible = visibleContentCondition
            inputField.isVisible = visibleContentCondition
            buttonBox.isVisible = visibleContentCondition

            attachedFileAdapter.files = state.attachedFiles

            error.isVisible = state.error != null
        }
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
                sharedViewModel.pickFile()
            }
            is ChatEffect.NavigateUp -> {
                findNavController().navigateUp()
            }
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding
            .bind(inflater.inflate(R.layout.fragment_chat, container, false))
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.fileUri.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { uri ->
                val (path, fileName) =
                    createFileAndGetPathWithFileNameFromCache(requireContext(), uri)
                val type = requireContext().contentResolver.getType(uri) ?: return@let
                store.accept(ChatEvent.Ui.AddAttachedFile(AttachedFile(fileName, type, path)))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sharedViewModel.fileUri.removeObservers(viewLifecycleOwner)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initMessageRecycler()
        initInputFileRecycler()
        initInputField()
        initErrorView()
    }

    private fun initToolbar() {
        binding.toolbarLayout.toolbar.apply {
            title = args.channel.name
        }

        binding.topicText.text = String.format(
            getString(R.string.topic_text),
            args.topic.name
        )

        binding.toolbarLayout.backButton.setOnClickListener {
            store.accept(ChatEvent.Ui.BackButtonClick)
        }
    }

    private fun initErrorView() {
        binding.error.setOnErrorButtonClickListener {
            store.accept(ChatEvent.Ui.RefreshFirstPage)
        }

        binding.error.text = requireContext().getString(R.string.error_text)
    }

    private fun initMessageRecycler() {
        initAdapter()
        binding.recyclerMessages.adapter = chatAdapter

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.recyclerMessages.layoutManager = layoutManager
    }

    private fun initAdapter() {
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
        }

        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart >= chatAdapter.itemCount - 1) {
                    binding.recyclerMessages.smoothScrollToPosition(chatAdapter.itemCount)
                }
            }
        })
    }

    private fun initInputFileRecycler() {
        attachedFileAdapter = AttachedFileAdapter {
            store.accept(ChatEvent.Ui.RemoveAttachedFile(it))
        }
        binding.recyclerInputFiles.adapter = attachedFileAdapter
        binding.recyclerInputFiles.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun initInputField() {
        binding.apply {
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
    }

    private fun showEmojiPick(messageId: Long) {
        EmojiPickFragment.newInstance(messageId)
            .show(childFragmentManager, EMOJI_PICK_FRAGMENT_TAG)
    }

    private fun showToast(stringResId: Int) {
        val text = requireContext().getString(stringResId)
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    override fun onEmojiPick(messageId: Long, emoji: Emoji) {
        store.accept(ChatEvent.Ui.UpdateReaction(messageId, emoji))
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
        private const val EMOJI_PICK_FRAGMENT_TAG = "TAG_EMOJI_PICK"
    }
}