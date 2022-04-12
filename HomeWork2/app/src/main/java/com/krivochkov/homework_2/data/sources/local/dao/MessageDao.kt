package com.krivochkov.homework_2.data.sources.local.dao

import androidx.room.*
import com.krivochkov.homework_2.data.sources.local.entity.MessageEntity
import io.reactivex.Single

@Dao
interface MessageDao {

    @Query("SELECT * FROM messages WHERE channelName = :channelName " +
            "AND topicName = :topicName ORDER BY date")
    fun getMessages(channelName: String, topicName: String): Single<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM messages WHERE channelName = :channelName AND topicName = :topicName")
    fun deleteMessages(channelName: String, topicName: String)

    @Transaction
    fun refreshMessages(channelName: String, topicName: String, newMessages: List<MessageEntity>) {
        deleteMessages(channelName, topicName)
        insertMessages(newMessages)
    }
}