package com.krivochkov.homework_2.data.mappers

import org.junit.Assert.*
import org.junit.Test
import com.krivochkov.homework_2.data.sources.remote.dto.UserDto

class UserMapperTest {

    @Test
    fun `mapToUser by default returns User item`() {
        val userDto = createUserDto()

        val user = userDto.mapToUser()

        assertEquals("", user.fullName)
        assertEquals("", user.avatarUrl)
        assertEquals("", user.status)
        assertEquals("", user.email)
        assertEquals(0, user.id)
    }

    @Test
    fun `mapToUser for id = 55 returns userId = 55`() {
        val userDto = createUserDto(id = 55)

        val user = userDto.mapToUser()

        assertEquals(55, user.id)
    }

    @Test
    fun `mapToUser for status = online returns userStatus = online`() {
        val userDto = createUserDto()
        val status = "online"

        val user = userDto.mapToUser(status = status)

        assertEquals("online", user.status)
    }

    @Test
    fun `mapToUser for fullName = Egor returns userFullName = Egor`() {
        val userDto = createUserDto(fullName = "Egor")

        val user = userDto.mapToUser()

        assertEquals("Egor", user.fullName)
    }

    private fun createUserDto(
        id: Long = 0,
        avatar: String? = "",
        email: String = "",
        fullName: String = "",
        isBot: Boolean = false
    ) = UserDto(id, avatar, email, fullName, isBot)
}