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
            userProvider = { Single.just(ownUser) }
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

    private fun createUserDto(
        id: Long = 0,
        avatar: String? = "",
        email: String = "",
        fullName: String = "",
        isBot: Boolean = false
    ) = UserDto(id, avatar, email, fullName, isBot)
}
