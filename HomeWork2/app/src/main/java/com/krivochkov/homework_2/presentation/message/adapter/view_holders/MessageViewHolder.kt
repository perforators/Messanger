package com.krivochkov.homework_2.presentation.message.adapter.view_holders

import android.view.Gravity
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.MessageItemBinding
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.message.adapter.items.MessageItem

class MessageViewHolder(
    private val binding: MessageItemBinding,
    private val onAddMyReaction: (messageId: Long, emoji: String) -> Unit,
    private val onRemoveMyReaction: (messageId: Long, emoji: String) -> Unit,
    private val onChoosingReaction: (messageId: Long) -> Unit
) : BaseViewHolder(binding.root) {

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