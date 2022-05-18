package com.krivochkov.homework_2.di.profile.modules

import com.krivochkov.homework_2.domain.use_cases.user.LoadMyUserUseCase
import com.krivochkov.homework_2.domain.use_cases.user.LoadMyUserUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface MyProfileDomainModule {

    @Binds
    fun bindLoadMyUserUseCase(impl: LoadMyUserUseCaseImpl): LoadMyUserUseCase
}