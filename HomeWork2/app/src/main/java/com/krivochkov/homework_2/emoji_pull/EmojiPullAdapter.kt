package com.krivochkov.homework_2.emoji_pull

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.databinding.EmojiItemBinding

class EmojiPullAdapter(
    private val listEmoji: List<String>,
    private val onEmojiClick: (String) -> Unit
) : RecyclerView.Adapter<EmojiPullAdapter.EmojiViewHolder>() {

    inner class EmojiViewHolder(private val binding: EmojiItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(emoji: String) {
            binding.emoji.apply {
                text = emoji
                setOnClickListener {
                    onEmojiClick(emoji)
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
            )
        )
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.bind(listEmoji[position])
    }

    override fun getItemCount() = listEmoji.size
}