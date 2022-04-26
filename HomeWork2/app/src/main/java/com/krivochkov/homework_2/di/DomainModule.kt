package com.krivochkov.homework_2.di

import com.krivochkov.homework_2.domain.use_cases.channel.LoadAllChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.LoadSubscribedChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.SearchChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.file.UploadAttachedFileUseCase
import com.krivochkov.homework_2.domain.use_cases.message.GetMessagesUseCase
import com.krivochkov.homework_2.domain.use_cases.message.GetSingleMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.message.SendMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.AddReactionUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.RemoveReactionUseCase
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.domain.use_cases.user.LoadAllUsersUseCase
import com.krivochkov.homework_2.domain.use_cases.user.LoadMyUserProfileUseCase
import com.krivochkov.homework_2.domain.use_cases.user.SearchUsersUseCase

class DomainModule(private val dataModule: DataModule) {

    val loadAllChannelsUseCase by lazy { LoadAllChannelsUseCase(dataModule.channelRepository) }

    val loadSubscribedChannelsUseCase by lazy {
        LoadSubscribedChannelsUseCase(dataModule.channelRepository)
    }

    val searchAllChannelsUseCase by lazy { SearchChannelsUseCase(loadAllChannelsUseCase) }

    val searchSubscribedChannelsUseCase by lazy { SearchChannelsUseCase(loadSubscribedChannelsUseCase) }

    val uploadAttachedFileUseCase by lazy {
        UploadAttachedFileUseCase(dataModule.attachedFileRepository)
    }

    val getMessagesUseCase by lazy { GetMessagesUseCase(dataModule.messageRepository) }

    val getSingleMessageUseCase by lazy { GetSingleMessageUseCase(dataModule.messageRepository) }

    val sendMessageUseCase by lazy {
        SendMessageUseCase(dataModule.messageRepository, uploadAttachedFileUseCase)
    }

    val addReactionUseCase by lazy { AddReactionUseCase(dataModule.messageRepository) }

    val removeReactionUseCase by lazy { RemoveReactionUseCase(dataModule.messageRepository) }

    val loadTopicsUseCase by lazy { LoadTopicsUseCase(dataModule.channelRepository) }

    val loadAllUsersUseCase by lazy { LoadAllUsersUseCase(dataModule.userRepository) }

    val searchUsersUseCase by lazy { SearchUsersUseCase(loadAllUsersUseCase) }

    val loadMyUserProfileUseCase by lazy { LoadMyUserProfileUseCase(dataModule.userRepository) }
}