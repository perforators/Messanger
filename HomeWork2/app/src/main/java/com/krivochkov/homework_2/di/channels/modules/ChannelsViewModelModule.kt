package com.krivochkov.homework_2.di.channels.modules

import com.krivochkov.homework_2.di.channels.annotations.AllChannels
import com.krivochkov.homework_2.di.channels.annotations.SubscribedChannels
import com.krivochkov.homework_2.presentation.SearchQueryFilter
import com.krivochkov.homework_2.presentation.channel.channels.ChannelsViewModelFactory
import com.krivochkov.homework_2.presentation.channel.elm.ChannelStoreFactory
import dagger.Module
import dagger.Provides

@Module
class ChannelsViewModelModule {

    @Provides
    @AllChannels
    fun provideAllChannelsViewModelFactory(
        @AllChannels channelsStoreFactory: ChannelStoreFactory,
        searchQueryFilter: SearchQueryFilter
    ): ChannelsViewModelFactory {
        return ChannelsViewModelFactory(channelsStoreFactory, searchQueryFilter)
    }

    @Provides
    @SubscribedChannels
    fun provideSubscribedChannelsViewModelFactory(
        @SubscribedChannels channelsStoreFactory: ChannelStoreFactory,
        searchQueryFilter: SearchQueryFilter
    ): ChannelsViewModelFactory {
        return ChannelsViewModelFactory(channelsStoreFactory, searchQueryFilter)
    }
}