package com.krivochkov.homework_2.data.sources.remote.data_sources

import com.krivochkov.homework_2.data.sources.remote.api.ZulipApi
import com.krivochkov.homework_2.data.sources.remote.api.ZulipApiProvider
import com.krivochkov.homework_2.data.sources.remote.dto.MessageDto
import com.krivochkov.homework_2.data.sources.remote.request.Request
import io.reactivex.Completable
import io.reactivex.Single

class MessageRemoteDataSourceImpl(
    private val api: ZulipApi = ZulipApiProvider.zulipApi
) : MessageRemoteDataSource {

    override fun getMessages(request: Request): Single<List<MessageDto>> {
        return api.getAllMessages(request.queryMap).map { it.messages }
    }

    override fun sendMessage(request: Request): Completable {
        return api.sendMessage(request.queryMap)
    }

    override fun addReaction(messageId: Long, emojiName: String): Completable {
        return api.addReaction(messageId, emojiName)
    }

    override fun removeReaction(messageId: Long, emojiName: String): Completable {
        return api.removeReaction(messageId, emojiName)
    }
}