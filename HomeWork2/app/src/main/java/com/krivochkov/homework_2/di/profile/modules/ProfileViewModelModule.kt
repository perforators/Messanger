package com.krivochkov.homework_2.di.profile.modules

import com.krivochkov.homework_2.presentation.profile.ProfileViewModelFactory
import com.krivochkov.homework_2.presentation.profile.elm.ProfileStoreFactory
import dagger.Module
import dagger.Provides

@Module
class ProfileViewModelModule {

    @Provides
    fun provideProfileViewModelFactory(
        profileStoreFactory: ProfileStoreFactory)
    : ProfileViewModelFactory {
        return ProfileViewModelFactory(profileStoreFactory)
    }
}