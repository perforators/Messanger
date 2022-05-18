package com.krivochkov.homework_2.di.channel.channels.modules

import com.krivochkov.homework_2.di.channel.channels.annotations.AllChannels
import com.krivochkov.homework_2.di.channel.channels.annotations.SubscribedChannels
import com.krivochkov.homework_2.domain.use_cases.channel.*
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [ChannelsDomainModule.BindsModule::class])
class ChannelsDomainModule {

    @Provides
    @AllChannels
    fun provideSearchAllChannelsUseCase(
        @AllChannels loadChannelsUseCase: LoadChannelsUseCase
    ): SearchChannelsUseCase {
        return SearchChannelsUseCaseImpl(loadChannelsUseCase)
    }

    @Provides
    @SubscribedChannels
    fun provideSearchSubscribedChannelsUseCase(
        @SubscribedChannels loadChannelsUseCase: LoadChannelsUseCase
    ): SearchChannelsUseCase {
        return SearchChannelsUseCaseImpl(loadChannelsUseCase)
    }

    @Module
    interface BindsModule {

        @Binds
        fun bindLoadTopicsUseCase(impl: LoadTopicsUseCaseImpl): LoadTopicsUseCase

        @Binds
        @AllChannels
        fun bindLoadAllChannelsUseCase(impl: LoadAllChannelsUseCase): LoadChannelsUseCase

        @Binds
        @SubscribedChannels
        fun bindLoadSubscribedChannelsUseCase(impl: LoadSubscribedChannelsUseCase): LoadChannelsUseCase
    }
}