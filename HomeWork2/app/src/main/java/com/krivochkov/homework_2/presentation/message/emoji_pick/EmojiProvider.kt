package com.krivochkov.homework_2.presentation.message.emoji_pick

import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.utils.convertEmojiToUtf

object EmojiProvider {

    private val emojiList = listOf(
        Emoji("grinning", "1f600".convertEmojiToUtf()),
        Emoji("smiley", "1f603".convertEmojiToUtf()),
        Emoji("big_smile", "1f604".convertEmojiToUtf()),
    )

    fun getAll(): List<Emoji> = emojiList
}