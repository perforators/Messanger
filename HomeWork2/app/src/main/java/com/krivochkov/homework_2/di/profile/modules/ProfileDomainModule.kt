package com.krivochkov.homework_2.di.profile.modules

import com.krivochkov.homework_2.domain.repositories.UserRepository
import com.krivochkov.homework_2.domain.use_cases.user.LoadMyUserProfileUseCase
import dagger.Module
import dagger.Provides

@Module
class ProfileDomainModule {

    @Provides
    fun provideLoadMyUserProfileUseCase(repository: UserRepository): LoadMyUserProfileUseCase {
        return LoadMyUserProfileUseCase(repository)
    }
}