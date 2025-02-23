package com.umtech.tawkandroid.data.model

import android.os.Parcel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserTest {

    @Test
    fun `User model initializes correctly`() {
        val user = User(
            login = "testUser",
            id = 123,
            avatarUrl = "https://example.com/avatar.png",
            siteAdmin = true,
            hasNotes = true
        )

        assertEquals("testUser", user.login)
        assertEquals(123, user.id)
        assertEquals("https://example.com/avatar.png", user.avatarUrl)
        assertEquals(true, user.siteAdmin)
        assertEquals(true, user.hasNotes)
    }

    @Test
    fun `User model default values are correct`() {
        val user = User()

        assertEquals(null, user.login)
        assertEquals(null, user.id)
        assertEquals(null, user.avatarUrl)
        assertEquals(null, user.siteAdmin)
        assertEquals(false, user.hasNotes) // Default value should be false
    }

    @Test
    fun `toEntity converts User to UserEntity correctly`() {
        val user = User(
            login = "testUser",
            id = 123,
            avatarUrl = "https://example.com/avatar.png",
            userViewType = "admin",
            hasNotes = true
        )

        val entity = user.toEntity()

        assertEquals(123, entity.id)
        assertEquals("testUser", entity.login)
        assertEquals("https://example.com/avatar.png", entity.avatarUrl)
        assertEquals("admin", entity.userViewType)
        assertEquals(true, entity.hasNotes)
    }

    @Test
    fun `toEntity handles null values correctly`() {
        val user = User()

        val entity = user.toEntity()

        assertEquals(0, entity.id) // Default value should be 0
        assertEquals("", entity.login) // Default value should be an empty string
        assertEquals("", entity.avatarUrl) // Default value should be an empty string
        assertEquals(null, entity.userViewType) // Should remain null
        assertEquals(false, entity.hasNotes) // Default should be false
    }
}