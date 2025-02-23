package com.umtech.tawkandroid.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class UserEntityTest {

    @Test
    fun `UserEntity initializes correctly`() {
        val userEntity = UserEntity(
            id = 123,
            login = "testUser",
            avatarUrl = "https://example.com/avatar.png",
            userViewType = "Admin",
            timestamp = 1700000000000L,
            hasNotes = true
        )

        assertEquals(123, userEntity.id)
        assertEquals("testUser", userEntity.login)
        assertEquals("https://example.com/avatar.png", userEntity.avatarUrl)
        assertEquals("Admin", userEntity.userViewType)
        assertEquals(1700000000000L, userEntity.timestamp)
        assertEquals(true, userEntity.hasNotes)
    }

    @Test
    fun `UserEntity default values are correct`() {
        val userEntity = UserEntity(
            id = null,
            login = null,
            avatarUrl = null,
            userViewType = null
        )

        assertEquals(null, userEntity.id)
        assertEquals(null, userEntity.login)
        assertEquals(null, userEntity.avatarUrl)
        assertEquals(null, userEntity.userViewType)
        assertNotNull(userEntity.timestamp) // Should be initialized with current time
        assertEquals(false, userEntity.hasNotes) // Default value should be false
    }

    @Test
    fun `toUser converts UserEntity to User correctly`() {
        val userEntity = UserEntity(
            id = 123,
            login = "testUser",
            avatarUrl = "https://example.com/avatar.png",
            userViewType = "Admin",
            hasNotes = true
        )

        val user = userEntity.toUser()

        assertEquals(123, user.id)
        assertEquals("testUser", user.login)
        assertEquals("https://example.com/avatar.png", user.avatarUrl)
        assertEquals("Admin", user.userViewType)
        assertEquals(true, user.hasNotes)
    }

    @Test
    fun `toUser handles null values correctly`() {
        val userEntity = UserEntity(
            id = null,
            login = null,
            avatarUrl = null,
            userViewType = null,
            hasNotes = false
        )

        val user = userEntity.toUser()

        assertEquals(null, user.id)
        assertEquals(null, user.login)
        assertEquals(null, user.avatarUrl)
        assertEquals(null, user.userViewType)
        assertEquals(false, user.hasNotes) // Default should be false
    }
}