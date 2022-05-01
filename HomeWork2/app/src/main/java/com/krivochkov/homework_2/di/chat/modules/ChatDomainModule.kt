package com.krivochkov.homework_2.di.chat.modules

import com.krivochkov.homework_2.domain.use_cases.file.UploadAttachedFileUseCase
import com.krivochkov.homework_2.domain.use_cases.file.UploadAttachedFileUseCaseImpl
import com.krivochkov.homework_2.domain.use_cases.message.*
import com.krivochkov.homework_2.domain.use_cases.reaction.AddReactionUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.AddReactionUseCaseImpl
import com.krivochkov.homework_2.domain.use_cases.reaction.RemoveReactionUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.RemoveReactionUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface ChatDomainModule {

    @Binds
    fun bindGetMessagesUseCase(impl: GetMessagesUseCaseImpl): GetMessagesUseCase

    @Binds
    fun bindGetSingleMessageUseCase(impl: GetSingleMessageUseCaseImpl): GetSingleMessageUseCase

    @Binds
    fun bindSendMessageUseCase(impl: SendMessageUseCaseImpl): SendMessageUseCase

    @Binds
    fun bindUploadAttachedFileUseCase(impl: UploadAttachedFileUseCaseImpl): UploadAttachedFileUseCase

    @Binds
    fun bindAddReactionUseCase(impl: AddReactionUseCaseImpl): AddReactionUseCase

    @Binds
    fun bindRemoveReactionUseCase(impl: RemoveReactionUseCaseImpl): RemoveReactionUseCase
}