package com.krivochkov.homework_2.presentation.chat.emoji_pick

import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.utils.convertEmojiToUtf

object EmojiProvider {

    private val emojiList = listOf(
        Emoji("grinning", "1f600".convertEmojiToUtf()),
        Emoji("smiley", "1f603".convertEmojiToUtf()),
        Emoji("factory", "1f3ed".convertEmojiToUtf()),
        Emoji("face_throwing_a_kiss", "1f618".convertEmojiToUtf()),
        Emoji("artist_palette", "1f3a8".convertEmojiToUtf()),
        Emoji("school", "1f3eb".convertEmojiToUtf()),
        Emoji("pig_face", "1f437".convertEmojiToUtf()),
        Emoji("hankey", "1f409".convertEmojiToUtf()),
        Emoji("cactus", "1f335".convertEmojiToUtf()),
        Emoji("cooking", "1f373".convertEmojiToUtf()),
        Emoji("pizza", "1f355".convertEmojiToUtf()),
        Emoji("cat_face_with_wry_smile", "1f63c".convertEmojiToUtf()),
        Emoji("sparkler", "1f387".convertEmojiToUtf()),
        Emoji("ambulance", "1f691".convertEmojiToUtf()),
        Emoji("dizzy_face", "1f635".convertEmojiToUtf()),
        Emoji("kimono", "1f458".convertEmojiToUtf()),
        Emoji("bread", "1f35e".convertEmojiToUtf()),
        Emoji("telescope", "1f52d".convertEmojiToUtf()),
        Emoji("face_with_head_bandage", "1f915".convertEmojiToUtf()),
        Emoji("spider", "1f577".convertEmojiToUtf()),
        Emoji("sweet_potato", "1f360".convertEmojiToUtf()),
        Emoji("ru", "1f1f7".convertEmojiToUtf()),
    )

    fun getAll(): List<Emoji> = emojiList
}