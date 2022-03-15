package com.krivochkov.homework_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.custom_views.EmojiProvider
import com.krivochkov.homework_2.databinding.ActivityMessagesBinding
import com.krivochkov.homework_2.emoji_pick.EmojiPickFragment
import com.krivochkov.homework_2.message_adapter.MessageAdapter
import com.krivochkov.homework_2.message_adapter.items.DateSeparatorItem
import com.krivochkov.homework_2.message_adapter.items.Item
import com.krivochkov.homework_2.message_adapter.items.MessageItem
import com.krivochkov.homework_2.models.Message
import com.krivochkov.homework_2.utils.convertToDate

class MessagesActivity : AppCompatActivity(), EmojiPickFragment.OnEmojiPickListener {

    private lateinit var viewBinding: ActivityMessagesBinding
    private lateinit var adapter: MessageAdapter
    private val viewModel: MessagesViewModel by viewModels()

    private val onChangeMyReaction: (Long, String) -> Unit = { messageId, emoji ->
        viewModel.updateReaction(messageId, emoji)
        viewModel.refreshMessages()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initToolbar()
        initRecycler()
        initInputField()

        viewModel.messages.observe(this) {
            adapter.submitList(it.toMessageItemsWithDates())
        }
    }

    private fun initToolbar() {
        viewBinding.toolbarLayout.toolbar.apply {
            title = getString(R.string.title_message_screen)
            setSupportActionBar(this)
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
                viewBinding.recyclerView.smoothScrollToPosition(adapter.itemCount)
            }
        })
        viewBinding.recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        viewBinding.recyclerView.layoutManager = layoutManager
    }

    private fun showEmojiPick(messageId: Long) {
        EmojiPickFragment.newInstance(EmojiProvider().getAll(), messageId)
            .show(supportFragmentManager, EMOJI_PICK_FRAGMENT_TAG)
    }

    private fun initInputField() {
        viewBinding.apply {
            sendButton.setOnClickListener {
                val inputText = inputField.text.toString()

                if (inputText.isNotEmpty()) {
                    inputField.setText("")
                    viewModel.sendMessage(inputText)
                    viewModel.refreshMessages()
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
            val messageItems = groups.value.map {
                it.groupedReactions
                MessageItem(it)
            }
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