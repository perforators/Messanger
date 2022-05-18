package com.krivochkov.homework_2.di.application.modules

import com.krivochkov.homework_2.data.repositories.AttachedFileRepositoryImpl
import com.krivochkov.homework_2.data.repositories.ChannelRepositoryImpl
import com.krivochkov.homework_2.data.repositories.MessageRepositoryImpl
import com.krivochkov.homework_2.data.repositories.UserRepositoryImpl
import com.krivochkov.homework_2.data.sources.local.data_sources.*
import com.krivochkov.homework_2.data.sources.remote.data_sources.*
import com.krivochkov.homework_2.domain.repositories.AttachedFileRepository
import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import com.krivochkov.homework_2.domain.repositories.MessageRepository
import com.krivochkov.homework_2.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindChannelRepository(impl: ChannelRepositoryImpl): ChannelRepository

    @Binds
    @Singleton
    fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @Binds
    @Singleton
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    fun bindAttachedFileRepository(impl: AttachedFileRepositoryImpl): AttachedFileRepository

    @Binds
    fun bindChannelLocalDataSource(impl: ChannelLocalDataSourceImpl): ChannelLocalDataSource

    @Binds
    fun bindMessageLocalDataSource(impl: MessageLocalDataSourceImpl): MessageLocalDataSource

    @Binds
    fun bindTopicLocalDataSource(impl: TopicLocalDataSourceImpl): TopicLocalDataSource

    @Binds
    fun bindChannelRemoteDataSource(impl: ChannelRemoteDataSourceImpl): ChannelRemoteDataSource

    @Binds
    fun bindFileRemoteDataSource(impl: FileRemoteDataSourceImpl): FileRemoteDataSource

    @Binds
    fun bindMessageRemoteDataSource(impl: MessageRemoteDataSourceImpl): MessageRemoteDataSource

    @Binds
    fun bindUserRemoteDataSource(impl: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    fun bindUserLocalDataSource(impl: UserLocalDataSourceImpl): UserLocalDataSource
}