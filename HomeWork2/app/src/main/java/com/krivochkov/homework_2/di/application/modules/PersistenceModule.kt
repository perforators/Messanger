package com.krivochkov.homework_2.di.application.modules

import android.content.Context
import androidx.room.Room
import com.krivochkov.homework_2.data.sources.local.dao.ChannelDao
import com.krivochkov.homework_2.data.sources.local.dao.MessageDao
import com.krivochkov.homework_2.data.sources.local.dao.TopicDao
import com.krivochkov.homework_2.data.sources.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    fun provideChannelDao(database: AppDatabase): ChannelDao = database.channelDao()

    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao = database.messageDao()

    @Provides
    fun provideTopicDao(database: AppDatabase): TopicDao = database.topicDao()

    companion object {
        private const val DB_NAME = "App database"
    }
}