package com.krivochkov.homework_2.message_adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.message_repository.MessageRepositoryImpl.Companion.MY_USER_ID
import com.krivochkov.homework_2.databinding.DateSeparatorItemBinding
import com.krivochkov.homework_2.databinding.MessageItemBinding
import com.krivochkov.homework_2.message_adapter.items.DateSeparatorItem
import com.krivochkov.homework_2.message_adapter.items.Item
import com.krivochkov.homework_2.message_adapter.items.MessageItem
import java.lang.IllegalStateException

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.BaseViewHolder>() {

    private val differ: AsyncListDiffer<Item> = AsyncListDiffer(this, DiffCallback())

    private var onAddMyReaction: (messageId: Long, emoji: String) -> Unit = { _, _ -> }
    private var onRemoveMyReaction: (messageId: Long, emoji: String) -> Unit = { _, _ -> }
    private var onChoosingReaction: (messageId: Long) -> Unit = {  }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class MessageViewHolder(private val binding: MessageItemBinding)
        : BaseViewHolder(binding.root) {

        fun bind(messageItem: MessageItem) {
            val message = messageItem.message

            binding.messageItem.gravity = if (message.isMeMessage) {
                Gravity.END
            } else {
                Gravity.START
            }

            binding.messageLayout.apply {
                removeAllEmoji()
                setAvatar(R.mipmap.ic_launcher_round) // пока что захардкожено
                setMessage(message.text)
                setUserName(message.userName)
                isMyMessage = message.isMeMessage
                setOnEmojiClickListener { emojiView, isSelected ->
                    when (isSelected) {
                        true -> onAddMyReaction(message.id, emojiView.emoji)
                        false -> onRemoveMyReaction(message.id, emojiView.emoji)
                    }
                }
                setOnPlusClickListener {
                    onChoosingReaction(message.id)
                }
                setOnLongClickListener {
                    onChoosingReaction(message.id)
                    true
                }

                if (message.reactions.isNotEmpty()) {
                    showPlus()
                } else {
                    hidePlus()
                }

                addReactions(message.groupedReactions)
            }
        }
    }

    class DateSeparatorViewHolder(private val binding: DateSeparatorItemBinding)
        : BaseViewHolder(binding.root) {

        fun bind(dateSeparatorItem: DateSeparatorItem) {
            binding.dateSeparator.text = dateSeparatorItem.date
        }
    }

    fun submitList(items: List<Item>, onCommitted: (() -> Unit)? = null) {
        differ.submitList(items, onCommitted)
    }

    fun setOnRemoveMyReactionListener(listener: (messageId: Long, emoji: String) -> Unit) {
        onRemoveMyReaction = listener
    }

    fun setOnAddMyReactionListener(listener: (messageId: Long, emoji: String) -> Unit) {
        onAddMyReaction = listener
    }

    fun setOnChoosingReactionListener(listener: (messageId: Long) -> Unit) {
        onChoosingReaction = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_MESSAGE -> MessageViewHolder(
                MessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
            )
            TYPE_DATE_SEPARATOR -> DateSeparatorViewHolder(
                DateSeparatorItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("Unknown viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> holder.bind(differ.currentList[position] as MessageItem)
            is DateSeparatorViewHolder -> holder.bind(differ.currentList[position] as DateSeparatorItem)
        }
    }

    override fun getItemViewType(position: Int) = differ.currentList[position].getType()

    override fun getItemCount() = differ.currentList.size

    companion object {
        const val TYPE_MESSAGE = 0
        const val TYPE_DATE_SEPARATOR = 1
    }
}