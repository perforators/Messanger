package com.krivochkov.homework_2.utils

private const val HEX_PREFIX = "0x"

//Example: "1f600".convertEmojiToUtf()
fun String.convertEmojiToUtf() =
    String(Character.toChars(Integer.decode("${HEX_PREFIX}$this")))