package com.krivochkov.homework_2.di

import com.krivochkov.homework_2.presentation.channel.elm.ChannelActor
import com.krivochkov.homework_2.presentation.channel.elm.ChannelStoreFactory
import com.krivochkov.homework_2.presentation.message.elm.MessageActor
import com.krivochkov.homework_2.presentation.message.elm.MessageStoreFactory
import com.krivochkov.homework_2.presentation.people.elm.PeopleActor
import com.krivochkov.homework_2.presentation.people.elm.PeopleStoreFactory
import com.krivochkov.homework_2.presentation.profile.elm.ProfileActor
import com.krivochkov.homework_2.presentation.profile.elm.ProfileStoreFactory

class PresentationModule(private val domainModule: DomainModule) {

    private val allChannelsActor by lazy {
        ChannelActor(
            domainModule.loadAllChannelsUseCase,
            domainModule.searchAllChannelsUseCase,
            domainModule.loadTopicsUseCase
        )
    }

    private val subscribedChannelsActor by lazy {
        ChannelActor(
            domainModule.loadSubscribedChannelsUseCase,
            domainModule.searchSubscribedChannelsUseCase,
            domainModule.loadTopicsUseCase
        )
    }

    private val peopleActor by lazy {
        PeopleActor(domainModule.searchUsersUseCase)
    }

    private val profileActor by lazy {
        ProfileActor(domainModule.loadMyUserProfileUseCase)
    }

    private val messageActor by lazy {
        MessageActor(
            domainModule.getMessagesUseCase,
            domainModule.getSingleMessageUseCase,
            domainModule.sendMessageUseCase,
            domainModule.addReactionUseCase,
            domainModule.removeReactionUseCase
        )
    }

    val allChannelsStoreFactory by lazy { ChannelStoreFactory(allChannelsActor) }

    val subscribedChannelsStoreFactory by lazy { ChannelStoreFactory(subscribedChannelsActor) }

    val peopleStoreFactory by lazy { PeopleStoreFactory(peopleActor) }

    val profileStoreFactory by lazy { ProfileStoreFactory(profileActor) }

    val messageStoreFactory by lazy { MessageStoreFactory(messageActor) }
}