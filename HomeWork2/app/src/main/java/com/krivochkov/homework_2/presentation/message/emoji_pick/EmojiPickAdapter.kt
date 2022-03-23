package com.krivochkov.homework_2.presentation.message.emoji_pick

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.databinding.EmojiItemBinding

class EmojiPickAdapter(
    private val listEmoji: List<String>,
    private val onEmojiPick: (String) -> Unit
) : RecyclerView.Adapter<EmojiPickAdapter.EmojiViewHolder>() {

    class EmojiViewHolder(
        private val binding: EmojiItemBinding,
        private val onEmojiPick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(emoji: String) {
            binding.emoji.apply {
                text = emoji
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