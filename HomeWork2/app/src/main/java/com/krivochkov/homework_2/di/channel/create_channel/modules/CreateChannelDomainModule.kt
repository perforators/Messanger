package com.krivochkov.homework_2.di.channel.create_channel.modules

import com.krivochkov.homework_2.domain.use_cases.channel.CheckChannelForExistenceUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.CheckChannelForExistenceUseCaseImpl
import com.krivochkov.homework_2.domain.use_cases.channel.SubscribeToChannelUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.SubscribeToChannelUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface CreateChannelDomainModule {

    @Binds
    fun bindCheckChannelForExistenceUseCase(
        impl: CheckChannelForExistenceUseCaseImpl
    ): CheckChannelForExistenceUseCase

    @Binds
    fun bindSubscribeToChannelUseCase(impl: SubscribeToChannelUseCaseImpl): SubscribeToChannelUseCase
}