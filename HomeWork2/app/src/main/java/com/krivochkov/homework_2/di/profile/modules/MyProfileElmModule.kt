package com.krivochkov.homework_2.di.profile.modules

import com.krivochkov.homework_2.domain.use_cases.user.LoadMyUserUseCase
import com.krivochkov.homework_2.presentation.profile.my_profile.elm.ProfileActor
import dagger.Module
import dagger.Provides

@Module
class MyProfileElmModule {

    @Provides
    fun provideProfileActor(loadMyUserUseCase: LoadMyUserUseCase): ProfileActor {
        return ProfileActor(loadMyUserUseCase)
    }
}