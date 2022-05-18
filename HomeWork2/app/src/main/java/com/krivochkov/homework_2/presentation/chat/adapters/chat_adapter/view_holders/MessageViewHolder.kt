package com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.view_holders

import android.view.Gravity
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.MessageItemBinding
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.MessageItem

class MessageViewHolder(
    private val binding: MessageItemBinding,
    private val onAddMyReaction: (messageId: Long, emoji: Emoji) -> Unit,
    private val onRemoveMyReaction: (messageId: Long, emoji: Emoji) -> Unit,
    private val onChoosingReaction: (messageId: Long) -> Unit
) : BaseViewHolder(binding.root) {

    fun bind(messageItem: MessageItem) {
        val message = messageItem.message

        binding.messageItem.gravity = if (message.isMyMessage) {
            Gravity.END
        } else {
            Gravity.START
        }

        binding.messageLayout.apply {
            removeAllEmoji()
            if (message.avatarUrl == null) {
                setAvatar(R.mipmap.ic_launcher)
            } else {
                setAvatar(message.avatarUrl)
            }
            setMessage(message.text)
            setUserName(message.userName)
            isMyMessage = message.isMyMessage
            setOnEmojiClickListener { emojiView, isSelected ->
                val emoji = message.groupedReactions.find { it.emoji.code == emojiView.emoji }?.emoji
                when (isSelected) {
                    true -> onAddMyReaction(message.id, emoji!!)
                    false -> onRemoveMyReaction(message.id, emoji!!)
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