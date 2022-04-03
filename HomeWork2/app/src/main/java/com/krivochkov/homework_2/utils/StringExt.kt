package com.krivochkov.homework_2.utils

fun String.hasNotWhitespaces() = !indices.all { this[it].isWhitespace() }