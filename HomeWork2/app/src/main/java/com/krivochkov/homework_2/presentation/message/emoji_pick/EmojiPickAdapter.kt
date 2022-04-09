package com.krivochkov.homework_2.presentation.message.emoji_pick

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.databinding.EmojiItemBinding
import com.krivochkov.homework_2.domain.models.Emoji

class EmojiPickAdapter(
    private val listEmoji: List<Emoji>,
    private val onEmojiPick: (Emoji) -> Unit
) : RecyclerView.Adapter<EmojiPickAdapter.EmojiViewHolder>() {

    class EmojiViewHolder(
        private val binding: EmojiItemBinding,
        private val onEmojiPick: (Emoji) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(emoji: Emoji) {
            binding.emoji.apply {
                text = emoji.code
                setOnClickListener {
                    onEmojiPick(emoji)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        return EmojiViewHolder(
            EmojiItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onEmojiPick
        )
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.bind(listEmoji[position])
    }

    override fun getItemCount() = listEmoji.size
}