package com.krivochkov.homework_2.data.sources.remote.data_sources

import com.krivochkov.homework_2.data.sources.remote.dto.MessageDto
import com.krivochkov.homework_2.data.sources.remote.request.Request
import io.reactivex.Completable
import io.reactivex.Single

interface MessageRemoteDataSource {

    fun getMessages(request: Request): Single<List<MessageDto>>

    fun sendMessage(request: Request): Completable

    fun addReaction(messageId: Long, emojiName: String): Completable

    fun removeReaction(messageId: Long, emojiName: String): Completable
}