package com.krivochkov.homework_2.presentation.message

import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.krivochkov.homework_2.databinding.FragmentMessageBinding
import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.presentation.message.adapters.attached_file_adapter.AttachedFileAdapter
import com.krivochkov.homework_2.presentation.message.adapters.message_adapter.MessageAdapter
import com.krivochkov.homework_2.presentation.message.emoji_pick.EmojiPickFragment
import com.krivochkov.homework_2.utils.createFileAndGetPathWithFileNameFromCache

class MessageFragment : Fragment(), EmojiPickFragment.OnEmojiPickListener {

    private val args by navArgs<MessageFragmentArgs>()

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var attachedFileAdapter: AttachedFileAdapter

    private val viewModel: MessageViewModel by viewModels {
        MessageViewModelFactory(args.channel.name, args.topic.name)
    }
    private val sharedViewModel: MessageSharedViewModel by activityViewModels()

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
                attachedFileAdapter.addFile(AttachedFile(fileName, type, path))
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
                messageAdapter.submitList(state.messagesWithDates) {
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
            is UIEvent.FailedRefreshMessages -> showToast(R.string.failed_refresh_messages)
            is UIEvent.ShowLoadingNextMessagePage -> messageAdapter.isLoading = true
            is UIEvent.HideLoadingNextMessagePage -> messageAdapter.isLoading = false
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
            viewModel.requestNextMessagePage(isRetry = true)
        }

        binding.error.text = requireContext().getString(R.string.error_text)
    }

    private fun initMessageRecycler() {
        initAdapter()
        binding.recyclerView.adapter = messageAdapter

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = layoutManager

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (lastItemPosition == LOADING_START_POSITION) {
                    viewModel.requestNextMessagePage()
                }
            }
        })
    }

    private fun initAdapter() {
        messageAdapter = MessageAdapter().apply {
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

        messageAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart >= messageAdapter.itemCount - 1) {
                    binding.recyclerView.smoothScrollToPosition(messageAdapter.itemCount)
                }
            }
        })
    }

    private fun initInputFileRecycler() {
        attachedFileAdapter = AttachedFileAdapter()
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
                    viewModel.sendMessage(inputText, attachedFileAdapter.files)
                    attachedFileAdapter.clearFiles()
                }
            }

            addFileButton.setOnClickListener {
                sharedViewModel.pickFile()
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
        viewModel.changeReaction(messageId, emoji)
    }

    companion object {

        private const val LOADING_START_POSITION = 5
        private const val EMOJI_PICK_FRAGMENT_TAG = "TAG_EMOJI_PICK"
    }
}