package com.krivochkov.homework_2.di.channels.modules

import com.krivochkov.homework_2.di.channels.annotations.ChannelsScreenScope
import com.krivochkov.homework_2.di.channels.annotations.AllChannels
import com.krivochkov.homework_2.di.channels.annotations.SubscribedChannels
import com.krivochkov.homework_2.domain.use_cases.channel.LoadChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.SearchChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.channel.elm.ChannelActor
import com.krivochkov.homework_2.presentation.channel.elm.ChannelStoreFactory
import dagger.Module
import dagger.Provides

@Module
class ChannelsElmModule {

    @Provides
    @AllChannels
    fun provideAllChannelsActor(
        @AllChannels loadChannelsUseCase: LoadChannelsUseCase,
        @AllChannels searchChannelsUseCase: SearchChannelsUseCase,
        loadTopicsUseCase: LoadTopicsUseCase
    ): ChannelActor {
        return ChannelActor(loadChannelsUseCase, searchChannelsUseCase, loadTopicsUseCase)
    }

    @Provides
    @SubscribedChannels
    fun provideSubscribedChannelsActor(
        @SubscribedChannels loadChannelsUseCase: LoadChannelsUseCase,
        @SubscribedChannels searchChannelsUseCase: SearchChannelsUseCase,
        loadTopicsUseCase: LoadTopicsUseCase
    ): ChannelActor {
        return ChannelActor(loadChannelsUseCase, searchChannelsUseCase, loadTopicsUseCase)
    }

    @Provides
    @ChannelsScreenScope
    @SubscribedChannels
    fun provideSubscribedChannelStoreFactory(@SubscribedChannels actor: ChannelActor): ChannelStoreFactory {
        return ChannelStoreFactory(actor)
    }

    @Provides
    @ChannelsScreenScope
    @AllChannels
    fun provideAllChannelStoreFactory(@AllChannels actor: ChannelActor): ChannelStoreFactory {
        return ChannelStoreFactory(actor)
    }
}