package com.krivochkov.homework_2.di.chat

import com.krivochkov.homework_2.domain.repositories.AttachedFileRepository
import com.krivochkov.homework_2.domain.repositories.MessageRepository

interface ChatScreenDependencies {

    fun getMessageRepository(): MessageRepository

    fun getAttachedFileRepository(): AttachedFileRepository
}