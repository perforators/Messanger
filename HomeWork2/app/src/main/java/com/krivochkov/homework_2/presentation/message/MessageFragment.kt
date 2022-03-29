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
import com.krivochkov.homework_2.presentation.custom_views.EmojiProvider
import com.krivochkov.homework_2.presentation.message.adapter.MessageAdapter
import com.krivochkov.homework_2.presentation.message.emoji_pick.EmojiPickFragment

class MessageFragment : Fragment(), EmojiPickFragment.OnEmojiPickListener {

    private val args by navArgs<MessageFragmentArgs>()

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MessageAdapter

    private val viewModel: MessageViewModel by viewModels()

    private val onChangeMyReaction: (Long, String) -> Unit = { messageId, emoji ->
        viewModel.updateReaction(messageId, emoji)
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
                adapter.submitList(state.messagesWithDates) {
                    showContent()
                }
            }
            is ScreenState.Loading -> showLoading()
            is ScreenState.Error -> showError()
        }
    }

    private fun handleEvent(event: UIEvent) {
        when (event) {
            is UIEvent.FailedSendMessage -> showToast(requireContext()
                .getString(R.string.failed_send_message))
            is UIEvent.FailedUpdateReaction -> showToast(requireContext()
                .getString(R.string.failed_update_reaction))
        }
    }

    private fun showLoading() {
        hideError()
        hideContent()
        changeVisibilityInputField(false)
        binding.loading.loadingLayout.startShimmer()
        binding.loading.loadingLayout.isVisible = true
    }

    private fun hideLoading() {
        binding.loading.loadingLayout.stopShimmer()
        binding.loading.loadingLayout.isVisible = false
    }

    private fun showError() {
        hideContent()
        hideLoading()
        changeVisibilityInputField(false)
        binding.error.isVisible = true
    }

    private fun hideError() {
        binding.error.isVisible = false
    }

    private fun showContent() {
        hideLoading()
        hideError()
        changeVisibilityInputField(true)
        binding.recyclerView.isVisible = true
    }

    private fun hideContent() {
        binding.recyclerView.isVisible = false
    }

    private fun changeVisibilityInputField(visibility: Boolean) {
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
            viewModel.refreshMessages(true, 3)
        }

        binding.error.text = requireContext().getString(R.string.error_text)
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
        EmojiPickFragment.newInstance(EmojiProvider().getAll(), messageId)
            .show(childFragmentManager, EMOJI_PICK_FRAGMENT_TAG)
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    override fun onEmojiPick(messageId: Long, emoji: String) {
        onChangeMyReaction(messageId, emoji)
    }

    companion object {
        private const val EMOJI_PICK_FRAGMENT_TAG = "TAG_EMOJI_PICK"
    }
}