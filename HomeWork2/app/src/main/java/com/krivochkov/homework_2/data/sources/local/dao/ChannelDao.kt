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

    @Query("DELETE FROM channels")
    fun deleteAllChannels()

    @Query("DELETE FROM channels WHERE isSubscribed = 1")
    fun deleteSubscribedChannels()

    @Transaction
    fun updateAllChannels(newChannels: List<ChannelEntity>) {
        deleteAllChannels()
        insertChannels(newChannels)
    }

    @Transaction
    fun updateSubscribedChannels(newChannels: List<ChannelEntity>) {
        deleteSubscribedChannels()
        insertChannels(newChannels)
    }
}