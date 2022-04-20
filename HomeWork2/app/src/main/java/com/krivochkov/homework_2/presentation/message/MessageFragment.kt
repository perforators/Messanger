package com.krivochkov.homework_2.presentation.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentMessageBinding
import com.krivochkov.homework_2.di.GlobalDI
import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.LoadingItem
import com.krivochkov.homework_2.presentation.message.adapters.attached_file_adapter.AttachedFileAdapter
import com.krivochkov.homework_2.presentation.message.adapters.message_adapter.MessageAdapter
import com.krivochkov.homework_2.presentation.message.adapters.message_adapter.PaginationAdapterHelper
import com.krivochkov.homework_2.presentation.message.adapters.message_adapter.items.DateSeparatorItem
import com.krivochkov.homework_2.presentation.message.adapters.message_adapter.items.MessageItem
import com.krivochkov.homework_2.presentation.message.elm.MessageEffect
import com.krivochkov.homework_2.presentation.message.elm.MessageEvent
import com.krivochkov.homework_2.presentation.message.elm.MessageState
import com.krivochkov.homework_2.presentation.message.emoji_pick.EmojiPickFragment
import com.krivochkov.homework_2.utils.convertToDate
import com.krivochkov.homework_2.utils.createFileAndGetPathWithFileNameFromCache
import vivid.money.elmslie.android.base.ElmFragment

class MessageFragment :
    EmojiPickFragment.OnEmojiPickListener, ElmFragment<MessageEvent, MessageEffect, MessageState>() {

    private val args by navArgs<MessageFragmentArgs>()

    private val channelName: String
        get() = args.channel.name

    private val topicName: String
        get() = args.topic.name

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var attachedFileAdapter: AttachedFileAdapter

    private val sharedViewModel: FilePickerSharedViewModel by activityViewModels()

    override val initEvent: MessageEvent
        get() = MessageEvent.Ui.Init(channelName, topicName)

    override fun createStore() =
        GlobalDI.INSTANCE.presentationModule.messageStoreFactory.provide()

    override fun render(state: MessageState) {
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

    override fun handleEffect(effect: MessageEffect) {
        when (effect) {
            is MessageEffect.CachedMessagesLoadError -> {
                showToast(R.string.failed_load_cached_messages)
            }
            is MessageEffect.NextPageLoadError -> {
                showToast(R.string.failed_load_next_page)
            }
            is MessageEffect.SendMessageError -> {
                showToast(R.string.failed_send_message)
            }
            is MessageEffect.AddReactionError -> {
                showToast(R.string.failed_add_reaction)
            }
            is MessageEffect.RemoveReactionError -> {
                showToast(R.string.failed_remove_reaction)
            }
            is MessageEffect.RefreshSingleMessageError -> {
                showToast(R.string.failed_refresh_message)
            }
            is MessageEffect.ShowEmojiPicker -> {
                showEmojiPick(effect.messageId)
            }
            is MessageEffect.ShowFilePicker -> {
                sharedViewModel.pickFile()
            }
            is MessageEffect.NavigateUp -> {
                findNavController().navigateUp()
            }
        }
    }

    override fun mapList(state: MessageState): List<Item> {
        val itemsList = state.items.toMutableList()
        return if (itemsList.contains(LoadingItem)) {
            listOf(LoadingItem) + itemsList.filterIsInstance<Message>().toMessageItemsWithDates()
        } else {
            itemsList.filterIsInstance<Message>().toMessageItemsWithDates()
        }
    }

    override fun renderList(state: MessageState, list: List<Any>) {
        messageAdapter.items = list.filterIsInstance<Item>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding
            .bind(inflater.inflate(R.layout.fragment_message, container, false))
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
                store.accept(MessageEvent.Ui.AddAttachedFile(AttachedFile(fileName, type, path)))
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
            store.accept(MessageEvent.Ui.BackButtonClick)
        }
    }

    private fun initErrorView() {
        binding.error.setOnErrorButtonClickListener {
            store.accept(MessageEvent.Ui.RefreshFirstPage)
        }

        binding.error.text = requireContext().getString(R.string.error_text)
    }

    private fun initMessageRecycler() {
        initAdapter()
        binding.recyclerMessages.adapter = messageAdapter

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.recyclerMessages.layoutManager = layoutManager
    }

    private fun initAdapter() {
        messageAdapter = MessageAdapter(
            PaginationAdapterHelper { store.accept(MessageEvent.Ui.LoadNextPage) }
        ).apply {
            setOnAddMyReactionListener { messageId, emoji ->
                store.accept(MessageEvent.Ui.AddReaction(messageId, emoji))
            }
            setOnRemoveMyReactionListener { messageId, emoji ->
                store.accept(MessageEvent.Ui.RemoveReaction(messageId, emoji))
            }
            setOnChoosingReactionListener { messageId ->
                store.accept(MessageEvent.Ui.ShowEmojiPicker(messageId))
            }
        }

        messageAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart >= messageAdapter.itemCount - 1) {
                    binding.recyclerMessages.smoothScrollToPosition(messageAdapter.itemCount)
                }
            }
        })
    }

    private fun initInputFileRecycler() {
        attachedFileAdapter = AttachedFileAdapter {
            store.accept(MessageEvent.Ui.RemoveAttachedFile(it))
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
                        MessageEvent.Ui.SendMessage(inputText)
                    )
                }
            }

            addFileButton.setOnClickListener {
                store.accept(MessageEvent.Ui.ShowFilePicker)
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
        store.accept(MessageEvent.Ui.UpdateReaction(messageId, emoji))
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