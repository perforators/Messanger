package com.krivochkov.homework_2.di

import android.app.Application
import androidx.room.Room
import com.krivochkov.homework_2.data.repositories.AttachedFileRepositoryImpl
import com.krivochkov.homework_2.data.repositories.ChannelRepositoryImpl
import com.krivochkov.homework_2.data.repositories.MessageRepositoryImpl
import com.krivochkov.homework_2.data.repositories.UserRepositoryImpl
import com.krivochkov.homework_2.data.sources.local.dao.ChannelDao
import com.krivochkov.homework_2.data.sources.local.dao.MessageDao
import com.krivochkov.homework_2.data.sources.local.dao.TopicDao
import com.krivochkov.homework_2.data.sources.local.data_sources.ChannelLocalDataSourceImpl
import com.krivochkov.homework_2.data.sources.local.data_sources.MessageLocalDataSourceImpl
import com.krivochkov.homework_2.data.sources.local.data_sources.TopicLocalDataSourceImpl
import com.krivochkov.homework_2.data.sources.local.database.AppDatabase
import com.krivochkov.homework_2.data.sources.remote.api.ZulipApi
import com.krivochkov.homework_2.data.sources.remote.api.ZulipApiProvider
import com.krivochkov.homework_2.data.sources.remote.data_sources.ChannelRemoteDataSourceImpl
import com.krivochkov.homework_2.data.sources.remote.data_sources.FileRemoteDataSourceImpl
import com.krivochkov.homework_2.data.sources.remote.data_sources.MessageRemoteDataSourceImpl
import com.krivochkov.homework_2.data.sources.remote.data_sources.UserRemoteDataSourceImpl

class DataModule(private val application: Application) {

    val attachedFileRepository by lazy { AttachedFileRepositoryImpl(fileRemoteDataSource) }

    val channelRepository by lazy {
        ChannelRepositoryImpl(channelRemoteDataSource, channelLocalDataSource, topicLocalDataSource)
    }

    val messageRepository by lazy {
        MessageRepositoryImpl(messageRemoteDataSource, messageLocalDataSource, userRepository)
    }

    val userRepository by lazy { UserRepositoryImpl(userRemoteDataSource) }

    private val api: ZulipApi by lazy { ZulipApiProvider.zulipApi }

    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
    }

    private val channelDao: ChannelDao by lazy { appDatabase.channelDao() }

    private val topicDao: TopicDao by lazy { appDatabase.topicDao() }

    private val messageDao: MessageDao by lazy { appDatabase.messageDao() }

    private val channelLocalDataSource by lazy { ChannelLocalDataSourceImpl(channelDao) }

    private val messageLocalDataSource by lazy { MessageLocalDataSourceImpl(messageDao) }

    private val topicLocalDataSource by lazy { TopicLocalDataSourceImpl(topicDao) }

    private val channelRemoteDataSource by lazy { ChannelRemoteDataSourceImpl(api) }

    private val fileRemoteDataSource by lazy { FileRemoteDataSourceImpl(api) }

    private val messageRemoteDataSource by lazy { MessageRemoteDataSourceImpl(api) }

    private val userRemoteDataSource by lazy { UserRemoteDataSourceImpl(api) }
}