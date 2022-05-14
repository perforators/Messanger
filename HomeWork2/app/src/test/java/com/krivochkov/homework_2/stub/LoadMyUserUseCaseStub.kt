package com.krivochkov.homework_2.stub

import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.domain.use_cases.user.LoadMyUserUseCase
import io.reactivex.Single

class LoadMyUserUseCaseStub : LoadMyUserUseCase {

    var userProvider: () -> Single<User> = {
        Single.just(User(id = 0, avatarUrl = "", email = "", fullName = "", status = "offline"))
    }

    override fun invoke(): Single<User> = userProvider()
}