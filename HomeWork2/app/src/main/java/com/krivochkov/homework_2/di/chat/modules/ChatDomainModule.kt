package com.krivochkov.homework_2.di.chat.modules

import com.krivochkov.homework_2.domain.repositories.AttachedFileRepository
import com.krivochkov.homework_2.domain.repositories.MessageRepository
import com.krivochkov.homework_2.domain.use_cases.file.UploadAttachedFileUseCase
import com.krivochkov.homework_2.domain.use_cases.message.GetMessagesUseCase
import com.krivochkov.homework_2.domain.use_cases.message.GetSingleMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.message.SendMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.AddReactionUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.RemoveReactionUseCase
import dagger.Module
import dagger.Provides

@Module
class ChatDomainModule {

    @Provides
    fun provideGetMessagesUseCase(repository: MessageRepository): GetMessagesUseCase {
        return GetMessagesUseCase(repository)
    }

    @Provides
    fun provideGetSingleMessageUseCase(repository: MessageRepository): GetSingleMessageUseCase {
        return GetSingleMessageUseCase(repository)
    }

    @Provides
    fun provideSendMessageUseCase(
        repository: MessageRepository,
        uploadAttachedFileUseCase: UploadAttachedFileUseCase
    ): SendMessageUseCase {
        return SendMessageUseCase(repository, uploadAttachedFileUseCase)
    }

    @Provides
    fun provideUploadAttachedFileUseCase(repository: AttachedFileRepository): UploadAttachedFileUseCase {
        return UploadAttachedFileUseCase(repository)
    }

    @Provides
    fun provideAddReactionUseCase(repository: MessageRepository): AddReactionUseCase {
        return AddReactionUseCase(repository)
    }

    @Provides
    fun provideRemoveReactionUseCase(repository: MessageRepository): RemoveReactionUseCase {
        return RemoveReactionUseCase(repository)
    }
}