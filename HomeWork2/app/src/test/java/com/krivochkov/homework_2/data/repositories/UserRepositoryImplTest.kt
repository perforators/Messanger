package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.data.sources.remote.dto.UserDto
import com.krivochkov.homework_2.stub.UserRemoteDataSourceStub
import com.krivochkov.homework_2.util.RxRule
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class UserRepositoryImplTest {

    @get:Rule
    val rxRule = RxRule()

    @Test
    fun `getMyUser by default returns own user`() {
        val ownUser = createUserDto(id = 55, email = "krivochkov01@mail.ru", fullName = "Egor")
        val userRemoteDataSource = UserRemoteDataSourceStub().apply {
            ownUserProvider = { Single.just(ownUser) }
        }
        val userRepository = UserRepositoryImpl(userRemoteDataSource)

        val testObserver = userRepository.getMyUser().test()

        testObserver.assertValue { user ->
            assertEquals("Egor", user.fullName)
            assertEquals("krivochkov01@mail.ru", user.email)
            assertEquals(55, user.id)
            true
        }
    }

    @Test
    fun `getUsers by default returns user list`() {
        val userList = listOf(
            createUserDto(id = 1, fullName = "Andrey", email = "andrey@mail.ru"),
            createUserDto(id = 2, fullName = "Egor", email = "egor@mail.ru"),
            createUserDto(id = 3, fullName = "Anton", email = "anton@mail.ru")
        )
        val userRemoteDataSource = UserRemoteDataSourceStub().apply {
            usersProvider = { Single.just(userList) }
        }
        val userRepository = UserRepositoryImpl(userRemoteDataSource)

        val testObserver = userRepository.getUsers().test()

        testObserver.assertValue { users ->
            assertEquals(3, users.size)
            assertEquals("Andrey", users[0].fullName)
            assertEquals("Egor", users[1].fullName)
            assertEquals("Anton", users[2].fullName)
            true
        }
    }

    private fun createUserDto(
        id: Long = 0,
        avatar: String? = "",
        email: String = "",
        fullName: String = "",
        isBot: Boolean = false
    ) = UserDto(id, avatar, email, fullName, isBot)
}
