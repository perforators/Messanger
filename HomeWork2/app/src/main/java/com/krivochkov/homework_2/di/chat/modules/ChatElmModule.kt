package com.krivochkov.homework_2.di.chat.modules

import com.krivochkov.homework_2.domain.use_cases.message.GetMessagesUseCase
import com.krivochkov.homework_2.domain.use_cases.message.GetSingleMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.message.SendMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.AddReactionUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.RemoveReactionUseCase
import com.krivochkov.homework_2.presentation.chat.elm.ChatActor
import dagger.Module
import dagger.Provides

@Module
class ChatElmModule {

    @Provides
    fun provideChatActor(
        getMessagesUseCase: GetMessagesUseCase,
        getSingleMessageUseCase: GetSingleMessageUseCase,
        sendMessageUseCase: SendMessageUseCase,
        addReactionUseCase: AddReactionUseCase,
        removeReactionUseCase: RemoveReactionUseCase
    ): ChatActor {
        return ChatActor(
            getMessagesUseCase,
            getSingleMessageUseCase,
            sendMessageUseCase,
            addReactionUseCase,
            removeReactionUseCase
        )
    }
}