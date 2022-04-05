package com.krivochkov.homework_2.presentation.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentMessageBinding
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.presentation.message.adapter.MessageAdapter
import com.krivochkov.homework_2.presentation.message.adapter.items.MessageItem
import com.krivochkov.homework_2.presentation.message.emoji_pick.EmojiPickFragment

class MessageFragment : Fragment(), EmojiPickFragment.OnEmojiPickListener {

    private val args by navArgs<MessageFragmentArgs>()

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MessageAdapter

    private val viewModel: MessageViewModel by viewModels {
        MessageViewModelFactory(args.channel.name, args.topic.name)
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
        initErrorView()

        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        viewModel.event.observe(viewLifecycleOwner) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let { event ->
                handleEvent(event)
            }
        }
    }

    private fun render(state: ScreenState) {
        when (state) {
            is ScreenState.MessagesLoaded -> {
                changeErrorVisibility(false)
                changeLoadingVisibility(false)
                changeInputFieldVisibility(true)
                adapter.submitList(state.messagesWithDates) {
                    changeContentVisibility(true)
                }
            }
            is ScreenState.Loading -> {
                changeErrorVisibility(false)
                changeContentVisibility(false)
                changeInputFieldVisibility(false)
                changeLoadingVisibility(true)
            }
            is ScreenState.Error -> {
                changeContentVisibility(false)
                changeInputFieldVisibility(false)
                changeLoadingVisibility(false)
                changeErrorVisibility(true)
            }
        }
    }

    private fun handleEvent(event: UIEvent) {
        when (event) {
            is UIEvent.FailedSendMessage -> showToast(R.string.failed_send_message)
            is UIEvent.FailedAddReaction -> showToast(R.string.failed_add_reaction)
            is UIEvent.FailedRemoveReaction -> showToast(R.string.failed_remove_reaction)
        }
    }

    private fun changeLoadingVisibility(visibility: Boolean) {
        binding.loading.loadingLayout.apply {
            isVisible = visibility
            if (visibility) startShimmer() else stopShimmer()
        }
    }

    private fun changeErrorVisibility(visibility: Boolean) {
        binding.error.isVisible = visibility
    }

    private fun changeContentVisibility(visibility: Boolean) {
        binding.recyclerView.isVisible = visibility
    }

    private fun changeInputFieldVisibility(visibility: Boolean) {
        binding.inputField.isVisible = visibility
        binding.buttonBox.isVisible = visibility
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

    private fun initErrorView() {
        binding.error.setOnErrorButtonClickListener {
            viewModel.refreshMessages()
        }

        binding.error.text = requireContext().getString(R.string.error_text)
    }

    private fun initRecycler() {
        initAdapter()
        binding.recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun initAdapter() {
        adapter = MessageAdapter().apply {
            setOnAddMyReactionListener { messageId, emoji ->
                viewModel.addReaction(messageId, emoji)
            }
            setOnRemoveMyReactionListener { messageId, emoji ->
                viewModel.removeReaction(messageId, emoji)
            }
            setOnChoosingReactionListener { messageId ->
                showEmojiPick(messageId)
            }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == adapter.itemCount - 1) {
                    binding.recyclerView.smoothScrollToPosition(adapter.itemCount)
                }
            }
        })
    }

    private fun initInputField() {
        binding.apply {
            sendButton.setOnClickListener {
                val inputText = inputField.text.toString()

                if (inputText.isNotEmpty()) {
                    inputField.setText("")
                    viewModel.sendMessage(inputText)
                }
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
        val messageItem = adapter.items.find { item ->
            item is MessageItem && item.message.id == messageId
        } as? MessageItem ?: return

        val groupedReaction = messageItem.message.groupedReactions.find {
            it.emoji.name == emoji.name
        }

        if (groupedReaction == null || !groupedReaction.isSelected) {
            viewModel.addReaction(messageId, emoji)
        } else {
            viewModel.removeReaction(messageId, emoji)
        }
    }

    companion object {

        private const val EMOJI_PICK_FRAGMENT_TAG = "TAG_EMOJI_PICK"
    }
}