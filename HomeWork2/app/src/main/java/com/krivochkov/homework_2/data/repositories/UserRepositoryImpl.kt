package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.domain.repositories.UserRepository
import io.reactivex.Single
import java.lang.IllegalStateException

class UserRepositoryImpl : UserRepository {

    private val users = listOf(
        User(
            0,
            "Egor Krivochkov",
            "krivochkov@mail.ru",
            "",
            true
        ),
        User(
            1,
            "Vlad Krivochkov",
            "vlad234@mail.ru",
            "",
            false
        ),
        User(
            2,
            "Kirill Zuev",
            "zuev@mail.ru",
            "",
            true
        ),
        User(
            3,
            "Egor Kireev",
            "kir894@mail.ru",
            "",
            true
        )
    )

    override fun loadUsers(): Single<List<User>> {
        return Single.fromCallable {
            randomException()
            users.map { it.copy() }
        }
    }

    override fun loadMyUser(): Single<User> {
        return Single.fromCallable {
            randomException()
            users[0].copy()
        }
    }

    private fun randomException() {
        if ((1..10).random() < 4) throw IllegalStateException("Random error")
    }
}