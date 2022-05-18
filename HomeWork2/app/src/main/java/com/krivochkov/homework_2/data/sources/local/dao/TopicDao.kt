package com.krivochkov.homework_2.data.sources.local.dao

import androidx.room.*
import com.krivochkov.homework_2.data.sources.local.entity.TopicEntity
import io.reactivex.Single

@Dao
interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopics(topics: List<TopicEntity>)

    @Query("SELECT * FROM topics WHERE channelId = :channelId")
    fun getTopics(channelId: Long): Single<List<TopicEntity>>

    @Query("DELETE FROM topics WHERE channelId = :channelId")
    fun deleteTopicsByChannelId(channelId: Long)

    @Transaction
    fun updateTopics(channelId: Long, newTopics: List<TopicEntity>) {
        deleteTopicsByChannelId(channelId)
        insertTopics(newTopics)
    }
}