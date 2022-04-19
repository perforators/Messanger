package com.krivochkov.homework_2.utils

fun String.convertEmojiToUtf() =
    this.split("-").fold("") { acc, s ->
        acc + String(Character.toChars(Integer.decode("0x$s")))
    }