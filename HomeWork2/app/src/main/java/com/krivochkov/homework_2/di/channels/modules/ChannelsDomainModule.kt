package com.krivochkov.homework_2.di.channels.modules

import com.krivochkov.homework_2.di.channels.annotations.AllChannels
import com.krivochkov.homework_2.di.channels.annotations.SubscribedChannels
import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import com.krivochkov.homework_2.domain.use_cases.channel.LoadAllChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.LoadChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.LoadSubscribedChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.SearchChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import dagger.Module
import dagger.Provides

@Module
class ChannelsDomainModule {

    @Provides
    @AllChannels
    fun provideLoadAllChannelsUseCase(repository: ChannelRepository): LoadChannelsUseCase {
        return LoadAllChannelsUseCase(repository)
    }

    @Provides
    @SubscribedChannels
    fun provideLoadSubscribedChannelsUseCase(repository: ChannelRepository): LoadChannelsUseCase {
        return LoadSubscribedChannelsUseCase(repository)
    }

    @Provides
    @AllChannels
    fun provideSearchAllChannelsUseCase(
        @AllChannels loadChannelsUseCase: LoadChannelsUseCase
    ): SearchChannelsUseCase {
        return SearchChannelsUseCase(loadChannelsUseCase)
    }

    @Provides
    @SubscribedChannels
    fun provideSearchSubscribedChannelsUseCase(
        @SubscribedChannels loadChannelsUseCase: LoadChannelsUseCase
    ): SearchChannelsUseCase {
        return SearchChannelsUseCase(loadChannelsUseCase)
    }

    @Provides
    fun provideLoadTopicsUseCase(repository: ChannelRepository): LoadTopicsUseCase {
        return LoadTopicsUseCase(repository)
    }
}