package com.krivochkov.homework_2.data.sources.local.dao

import androidx.room.*
import com.krivochkov.homework_2.data.sources.local.entity.ChannelEntity
import io.reactivex.Single

@Dao
interface ChannelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChannels(channels: List<ChannelEntity>)

    @Query("SELECT * FROM channels")
    fun getAllChannels(): Single<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE isSubscribed = 1")
    fun getSubscribedChannels(): Single<List<ChannelEntity>>

    @Query("DELETE FROM channels WHERE isSubscribed = :subscribed")
    fun deleteChannelsByCategory(subscribed: Boolean)

    @Transaction
    fun refreshChannels(subscribed: Boolean, newChannels: List<ChannelEntity>) {
        deleteChannelsByCategory(subscribed)
        insertChannels(newChannels)
    }
}