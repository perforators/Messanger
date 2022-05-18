package com.krivochkov.homework_2.di.channel.create_channel.modules

import com.krivochkov.homework_2.domain.use_cases.channel.CheckChannelForExistenceUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.SubscribeToChannelUseCase
import com.krivochkov.homework_2.presentation.channel.create_channel.elm.CreateChannelActor
import dagger.Module
import dagger.Provides

@Module
class CreateChannelElmModule {

    @Provides
    fun provideCreateChannelActor(
        checkChannelForExistenceUseCase: CheckChannelForExistenceUseCase,
        subscribeToChannelUseCase: SubscribeToChannelUseCase
    ) = CreateChannelActor(checkChannelForExistenceUseCase, subscribeToChannelUseCase)
}