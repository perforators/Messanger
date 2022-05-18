package com.krivochkov.homework_2.di.channel.channels

import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import com.krivochkov.homework_2.presentation.SearchQueryFilter

interface ChannelsScreenDependencies {

    fun getChannelRepository(): ChannelRepository

    fun getSearchQueryFilter(): SearchQueryFilter
}