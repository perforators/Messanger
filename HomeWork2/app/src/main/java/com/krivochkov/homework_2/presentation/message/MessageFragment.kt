package com.krivochkov.homework_2.presentation.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentMessageBinding
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.custom_views.EmojiProvider
import com.krivochkov.homework_2.presentation.message.adapter.MessageAdapter
import com.krivochkov.homework_2.presentation.message.emoji_pick.EmojiPickFragment
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.presentation.message.adapter.items.DateSeparatorItem
import com.krivochkov.homework_2.presentation.message.adapter.items.MessageItem
import com.krivochkov.homework_2.utils.convertToDate

class MessageFragment : Fragment(), EmojiPickFragment.OnEmojiPickListener {

    private val args by navArgs<MessageFragmentArgs>()

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MessageAdapter

    private val viewModel: MessageViewModel by viewModels()

    private val onChangeMyReaction: (Long, String) -> Unit = { messageId, emoji ->
        viewModel.updateReaction(messageId, emoji)
        viewModel.loadMessages()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initRecycler()
        initInputField()

        viewModel.messages.observe(this) {
            adapter.submitList(it.toMessageItemsWithDates())
        }
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
            findNavController().navigateUp()
        }
    }

    private fun initRecycler() {
        adapter = MessageAdapter().apply {
            setOnAddMyReactionListener(onChangeMyReaction)
            setOnRemoveMyReactionListener(onChangeMyReaction)
            setOnChoosingReactionListener { messageId ->
                showEmojiPick(messageId)
            }
        }
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.recyclerView.smoothScrollToPosition(adapter.itemCount)
            }
        })
        binding.recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun showEmojiPick(messageId: Long) {
        EmojiPickFragment.newInstance(EmojiProvider().getAll(), messageId)
            .show(childFragmentManager, EMOJI_PICK_FRAGMENT_TAG)
    }

    private fun initInputField() {
        binding.apply {
            sendButton.setOnClickListener {
                val inputText = inputField.text.toString()

                if (inputText.isNotEmpty()) {
                    inputField.setText("")
                    viewModel.sendMessage(inputText)
                    viewModel.loadMessages()
                }
            }

            inputField.doOnTextChanged { _, _, _, _ ->
                sendButton.isVisible = inputField.text.isNotEmpty()
                addFileButton.isVisible = inputField.text.isEmpty()
            }
        }
    }

    override fun onEmojiPick(messageId: Long, emoji: String) {
        onChangeMyReaction(messageId, emoji)
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
        private const val EMOJI_PICK_FRAGMENT_TAG = "TAG_EMOJI_PICK"
        private const val SECONDS_IN_DAY = 86400
    }
}