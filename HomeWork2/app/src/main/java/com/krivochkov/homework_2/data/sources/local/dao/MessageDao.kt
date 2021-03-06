package com.krivochkov.homework_2.data.sources.local.dao

import androidx.room.*
import com.krivochkov.homework_2.data.sources.local.entity.MessageEntity
import io.reactivex.Single

@Dao
interface MessageDao {

    @Query("SELECT * FROM messages WHERE channelName = :channelName " +
            "AND topicName = :topicName ORDER BY date")
    fun getAllMessagesFromTopic(channelName: String, topicName: String): Single<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE channelName = :channelName ORDER BY date")
    fun getAllMessagesFromChannel(channelName: String): Single<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM messages WHERE channelName = :channelName AND topicName = :topicName")
    fun deleteMessagesInTopic(channelName: String, topicName: String)

    @Transaction
    fun updateMessagesInTopic(
        channelName: String,
        topicName: String,
        newMessages: List<MessageEntity>
    ) {
        deleteMessagesInTopic(channelName, topicName)
        insertMessages(newMessages)
    }
}