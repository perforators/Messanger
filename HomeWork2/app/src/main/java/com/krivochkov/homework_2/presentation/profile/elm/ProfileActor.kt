package com.krivochkov.homework_2.presentation.profile.elm

import com.krivochkov.homework_2.domain.use_cases.user.LoadMyUserUseCase
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class ProfileActor(
    private val loadMyUserUseCase: LoadMyUserUseCase
) : ActorCompat<ProfileCommand, ProfileEvent> {

    override fun execute(command: ProfileCommand): Observable<ProfileEvent> = when (command) {
        is ProfileCommand.LoadMyProfile -> loadMyUserUseCase()
            .mapEvents(
                { profile -> ProfileEvent.Internal.ProfileLoaded(profile) },
                { error -> ProfileEvent.Internal.ErrorLoadingProfile(error) }
            )
    }
}