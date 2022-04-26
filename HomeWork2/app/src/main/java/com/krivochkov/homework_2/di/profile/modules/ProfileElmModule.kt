package com.krivochkov.homework_2.di.profile.modules

import com.krivochkov.homework_2.di.profile.annotations.ProfileScreenScope
import com.krivochkov.homework_2.domain.use_cases.user.LoadMyUserProfileUseCase
import com.krivochkov.homework_2.presentation.profile.elm.ProfileActor
import com.krivochkov.homework_2.presentation.profile.elm.ProfileStoreFactory
import dagger.Module
import dagger.Provides

@Module
class ProfileElmModule {

    @Provides
    fun provideProfileActor(loadMyUserProfileUseCase: LoadMyUserProfileUseCase): ProfileActor {
        return ProfileActor(loadMyUserProfileUseCase)
    }

    @Provides
    @ProfileScreenScope
    fun provideProfileStoreFactory(actor: ProfileActor): ProfileStoreFactory {
        return ProfileStoreFactory(actor)
    }
}