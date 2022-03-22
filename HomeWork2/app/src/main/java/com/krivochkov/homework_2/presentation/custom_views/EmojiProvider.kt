package com.krivochkov.homework_2.presentation.custom_views

class EmojiProvider {

    private val listEmoji = mapOf(
        "smiling_1" to "\uD83D\uDE00",
        "smiling_2" to "\uD83D\uDE03",
        "smiling_3" to "\uD83D\uDE04",
        "smiling_4" to "\uD83D\uDE01",
        "affection_1" to "\uD83E\uDD70",
        "affection_2" to "\uD83E\uDD29",
        "tongue_1" to "\uD83D\uDE0B",
        "tongue_2" to "\uD83E\uDD2A",
        "concerned_1" to "\uD83D\uDE32",
        "concerned_2" to "\uD83D\uDE30"
    )

    operator fun get(key: String) = if (listEmoji.containsKey(key)) {
        listEmoji[key]
    } else {
        null
    }

    fun getRandom() = getAll().random()

    fun getAll(): List<String> = listEmoji.values.toList()
}