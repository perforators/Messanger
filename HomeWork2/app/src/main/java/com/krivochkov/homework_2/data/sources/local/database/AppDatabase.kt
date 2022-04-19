package com.krivochkov.homework_2.data.sources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.krivochkov.homework_2.data.sources.local.converters.GroupedReactionsListConverter
import com.krivochkov.homework_2.data.sources.local.converters.ReactionsListConverter
import com.krivochkov.homework_2.data.sources.local.dao.ChannelDao
import com.krivochkov.homework_2.data.sources.local.dao.MessageDao
import com.krivochkov.homework_2.data.sources.local.dao.TopicDao
import com.krivochkov.homework_2.data.sources.local.entity.ChannelEntity
import com.krivochkov.homework_2.data.sources.local.entity.MessageEntity
import com.krivochkov.homework_2.data.sources.local.entity.TopicEntity

@Database(
    entities = [ChannelEntity::class, TopicEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ReactionsListConverter::class, GroupedReactionsListConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun channelDao(): ChannelDao

    abstract fun topicDao(): TopicDao

    abstract fun messageDao(): MessageDao

    companion object {
        const val DB_NAME = "App database"
    }
}