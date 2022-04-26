package com.krivochkov.homework_2.di.chat.modules

import com.krivochkov.homework_2.presentation.chat.ChatViewModelFactory
import com.krivochkov.homework_2.presentation.chat.elm.ChatStoreFactory
import dagger.Module
import dagger.Provides

@Module
class ChatViewModelModule {

    @Provides
    fun provideChatViewModelFactory(chatStoreFactory: ChatStoreFactory): ChatViewModelFactory {
        return ChatViewModelFactory(chatStoreFactory)
    }
}