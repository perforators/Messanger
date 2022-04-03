package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.domain.repositories.MessageRepository
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.models.Reaction
import io.reactivex.Completable
import io.reactivex.Single
import java.lang.IllegalStateException

class MessageRepositoryImpl : MessageRepository {

    private val messages = mutableListOf(
        Message(
            id = 1,
            userId = 435,
            userName = "Egor Krivochkov",
            avatarUrl = "",
            isMeMessage = true,
            text = "Привет!",
            date = 1644679406,
            reactions = mutableListOf()
        ),
        Message(
            id = 2,
            userId = 54,
            userName = "Anton Evushko",
            avatarUrl = "",
            isMeMessage = false,
            text = "Привет)",
            date = 1646954606,
            reactions = mutableListOf(
                Reaction(5435, "\uD83D\uDE00"),
                Reaction(5436, "\uD83D\uDE00"),
                Reaction(435, "\uD83D\uDE00"),
                Reaction(54332, "\uD83D\uDE00"),
                Reaction(54, "\uD83D\uDE01")
            )
        ),
        Message(
            id = 3,
            userId = 435,
            userName = "Egor Krivochkov",
            avatarUrl = "",
            isMeMessage = true,
            text = "Как дела?",
            date = 1646983406,
            reactions = mutableListOf()
        ),
        Message(
            id = 4,
            userId = 54,
            userName = "Anton Evushko",
            avatarUrl = "",
            isMeMessage = false,
            text = "Нормально",
            date = 1647069806,
            reactions = mutableListOf()
        ),
        Message(
            id = 5,
            userId = 56,
            userName = "Vlad Krivochkov",
            avatarUrl = "",
            isMeMessage = false,
            text = "Всем привет!",
            date = 1647077006,
            reactions = mutableListOf()
        ),
        Message(
            id = 6,
            userId = 435,
            userName = "Egor Krivochkov",
            avatarUrl = "",
            isMeMessage = true,
            text = "Привет!",
            date = 1647098606,
            reactions = mutableListOf()
        )
    )

    override fun getAllMessages(): Single<List<Message>> {
        return Single.fromCallable {
            messages.map { it.copy(reactions = it.reactions.toMutableList()) }
        }
    }

    override fun sendMessage(content: String): Completable {
        return Completable.fromCallable {
            randomException()
            val message = Message(
                id = messages.last().id + 1,
                userId = MY_USER_ID,
                userName = "Egor Krivochkov",
                avatarUrl = "",
                isMeMessage = true,
                text = content,
                date = System.currentTimeMillis() / 1000,
                reactions = mutableListOf()
            )

            messages.add(message)
        }
    }

    override fun updateReaction(messageId: Long, emoji: String): Completable {
        return Completable.fromCallable {
            randomException()
            val message = messages.find { it.id == messageId }
                ?: throw IllegalStateException("Message not found")
            val oldReaction = message.reactions.find { it.emoji == emoji && it.userId == MY_USER_ID }

            if (oldReaction != null) {
                message.reactions.remove(oldReaction)
            } else {
                message.reactions.add(Reaction(MY_USER_ID, emoji))
            }
        }
    }

    private fun randomException() {
        if ((1..10).random() < 3) throw IllegalStateException("Random error")
    }

    companion object {

        const val MY_USER_ID = 435
    }
}