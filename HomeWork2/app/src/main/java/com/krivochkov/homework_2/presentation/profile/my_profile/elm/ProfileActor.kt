package com.krivochkov.homework_2.presentation.profile.my_profile.elm

import com.krivochkov.homework_2.domain.use_cases.user.LoadMyUserUseCase
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class ProfileActor(
    private val loadMyUserUseCase: LoadMyUserUseCase
) : ActorCompat<MyProfileCommand, MyProfileEvent> {

    override fun execute(command: MyProfileCommand): Observable<MyProfileEvent> = when (command) {
        is MyProfileCommand.LoadMyProfile -> loadMyUserUseCase()
            .mapEvents(
                { profile -> MyProfileEvent.Internal.ProfileLoaded(profile) },
                { error -> MyProfileEvent.Internal.ErrorLoadingProfile(error) }
            )
    }
}