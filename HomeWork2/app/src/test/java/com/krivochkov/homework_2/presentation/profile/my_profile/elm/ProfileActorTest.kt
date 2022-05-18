package com.krivochkov.homework_2.presentation.profile.my_profile.elm

import android.accounts.NetworkErrorException
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.stub.LoadMyUserUseCaseStub
import com.krivochkov.homework_2.util.RxRule
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

class ProfileActorTest {

    @get:Rule
    val rxRule = RxRule()

    @Test
    fun `execute for LoadMyProfile command returns ProfileLoaded event with own user`() {
        val ownUser = createUser(id = 55, email = "krivochkov01@mail.ru", fullName = "Egor")
        val loadMyUserUseCase = LoadMyUserUseCaseStub().apply {
            userProvider = { Single.just(ownUser) }
        }
        val profileActor = ProfileActor(loadMyUserUseCase)

        val testObserver = profileActor.execute(MyProfileCommand.LoadMyProfile).test()

        testObserver.assertValue { event ->
            assertTrue(event is MyProfileEvent.Internal.ProfileLoaded)
            event as MyProfileEvent.Internal.ProfileLoaded
            assertEquals(ownUser, event.profile)
            true
        }
    }

    @Test
    fun `execute for LoadMyProfile command and by throwing out an error returns ErrorLoadingProfile event`() {
        val loadMyUserUseCase = LoadMyUserUseCaseStub().apply {
            userProvider = { Single.error(NetworkErrorException()) }
        }
        val profileActor = ProfileActor(loadMyUserUseCase)

        val testObserver = profileActor.execute(MyProfileCommand.LoadMyProfile).test()

        testObserver.assertValue { event ->
            assertTrue(event is MyProfileEvent.Internal.ErrorLoadingProfile)
            true
        }
    }

    private fun createUser(
        id: Long = 0,
        avatarUrl: String? = "",
        email: String = "",
        fullName: String = "",
        status: String = "offline"
    ) = User(id, fullName, email, avatarUrl, status)
}