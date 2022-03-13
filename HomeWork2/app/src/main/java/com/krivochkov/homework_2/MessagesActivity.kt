package com.krivochkov.homework_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.krivochkov.homework_2.custom_views.EmojiProvider
import com.krivochkov.homework_2.databinding.ActivityMessagesBinding
import com.krivochkov.homework_2.emoji_pull.EmojiPullFragment
import com.krivochkov.homework_2.message_adapter.MessageAdapter
import com.krivochkov.homework_2.message_adapter.items.DateSeparatorItem
import com.krivochkov.homework_2.message_adapter.items.Item
import com.krivochkov.homework_2.message_adapter.items.MessageItem
import com.krivochkov.homework_2.models.Message
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MessagesActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMessagesBinding
    private lateinit var adapter: MessageAdapter
    private lateinit var viewModel: MessagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewModel = ViewModelProvider(this)[MessagesViewModel::class.java]

        initToolbar()
        initRecycler()
        initInputField()

        viewModel.messages.observe(this) {
            val onCommitted = if (it.isLastActionSending) {
                { viewBinding.recyclerView.smoothScrollToPosition(adapter.itemCount) }
            } else {
                null
            }
            adapter.submitList(it.messages.toMessageItemsWithDates(), onCommitted)
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
            val onChangeMyReaction: (Long, String) -> Unit = { messageId, emoji ->
                viewModel.updateReaction(messageId, emoji)
                viewModel.refreshMessages()
            }

            setOnAddMyReactionListener(onChangeMyReaction)
            setOnRemoveMyReactionListener(onChangeMyReaction)
            setOnChoosingReactionListener { messageId ->
                showEmojiPull { emoji ->
                    onChangeMyReaction(messageId, emoji)
                }
            }
        }
        viewBinding.recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        viewBinding.recyclerView.layoutManager = layoutManager
    }

    private fun showEmojiPull(onEmojiClick: (String) -> Unit) {
        EmojiPullFragment.createInstance(EmojiProvider().getAll()) {
            onEmojiClick(it)
        }.show(supportFragmentManager, "TAG_EMOJI_PULL")
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
                if (inputField.text.isEmpty()) {
                    sendButton.visibility = View.GONE
                    addFileButton.visibility = View.VISIBLE
                } else {
                    sendButton.visibility = View.VISIBLE
                    addFileButton.visibility = View.GONE
                }
            }
        }
    }

    private fun List<Message>.toMessageItemsWithDates(): List<Item> {
        val listItems = mutableListOf<Item>()
        val groupedMessages = groupBy { (it.date / SECONDS_IN_DAY) * SECONDS_IN_DAY }

        for (groups in groupedMessages) {
            val date = Date(TimeUnit.SECONDS.toMillis(groups.key))
            val formattedDate = SimpleDateFormat("dd MMM", Locale("ru")).format(date)
            val messageItems = groups.value.map {
                MessageItem(it)
            }
            listItems += DateSeparatorItem(date.time, formattedDate)
            listItems += messageItems
        }

        return listItems
    }

    companion object {

        private const val SECONDS_IN_DAY = 86400
    }
}