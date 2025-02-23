package com.umtech.tawkandroid.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class UserDetailEntityTest{
    @Test
    fun `UserDetailEntity initializes correctly`() {
        val userDetailEntity = UserDetailEntity(
            login = "testUser",
            id = 123,
            avatarUrl = "https://example.com/avatar.png",
            type = "User",
            userViewType = "Admin",
            name = "Test User",
            company = "Tech Corp",
            blog = "https://blog.example.com",
            email = "test@example.com",
            bio = "Software Developer",
            twitterUsername = "testTwitter",
            followers = 100,
            following = 50,
            notes = "Important user"
        )

        assertEquals("testUser", userDetailEntity.login)
        assertEquals(123, userDetailEntity.id)
        assertEquals("https://example.com/avatar.png", userDetailEntity.avatarUrl)
        assertEquals("User", userDetailEntity.type)
        assertEquals("Admin", userDetailEntity.userViewType)
        assertEquals("Test User", userDetailEntity.name)
        assertEquals("Tech Corp", userDetailEntity.company)
        assertEquals("https://blog.example.com", userDetailEntity.blog)
        assertEquals("test@example.com", userDetailEntity.email)
        assertEquals("Software Developer", userDetailEntity.bio)
        assertEquals("testTwitter", userDetailEntity.twitterUsername)
        assertEquals(100, userDetailEntity.followers)
        assertEquals(50, userDetailEntity.following)
        assertEquals("Important user", userDetailEntity.notes)
    }

    @Test
    fun `UserDetailEntity default values are correct`() {
        val userDetailEntity = UserDetailEntity(
            login = "testUser",
            id = null,
            avatarUrl = null,
            type = null,
            userViewType = null,
            name = null,
            company = null,
            blog = null,
            email = null,
            bio = null,
            twitterUsername = null,
            followers = null,
            following = null
        )

        assertEquals("testUser", userDetailEntity.login)
        assertEquals(null, userDetailEntity.id)
        assertEquals(null, userDetailEntity.avatarUrl)
        assertEquals(null, userDetailEntity.type)
        assertEquals(null, userDetailEntity.userViewType)
        assertEquals(null, userDetailEntity.name)
        assertEquals(null, userDetailEntity.company)
        assertEquals(null, userDetailEntity.blog)
        assertEquals(null, userDetailEntity.email)
        assertEquals(null, userDetailEntity.bio)
        assertEquals(null, userDetailEntity.twitterUsername)
        assertEquals(null, userDetailEntity.followers)
        assertEquals(null, userDetailEntity.following)
        assertEquals("", userDetailEntity.notes) // Default value should be an empty string
    }

    @Test
    fun `toUserDetails converts UserDetailEntity to UserDetails correctly`() {
        val userDetailEntity = UserDetailEntity(
            login = "testUser",
            id = 123,
            avatarUrl = "https://example.com/avatar.png",
            name = "Test User",
            company = "Tech Corp",
            blog = "https://blog.example.com",
            followers = 100,
            following = 50,
            notes = "Important user",
            type = null,
            userViewType = null,
            email = null,
            bio = null,
            twitterUsername = null
        )

        val userDetails = userDetailEntity.toUserDetails()

        assertEquals("testUser", userDetails.login)
        assertEquals(123, userDetails.id)
        assertEquals("https://example.com/avatar.png", userDetails.avatarUrl)
        assertEquals("Test User", userDetails.name)
        assertEquals("Tech Corp", userDetails.company)
        assertEquals("https://blog.example.com", userDetails.blog)
        assertEquals(100, userDetails.followers)
        assertEquals(50, userDetails.following)
        assertEquals("Important user", userDetails.notes)
    }

    @Test
    fun `toUserDetails handles null values correctly`() {
        val userDetailEntity = UserDetailEntity(
            login = "testUser",
            id = null,
            avatarUrl = null,
            name = null,
            company = null,
            blog = null,
            followers = null,
            following = null,
            notes = null,
            type = null,
            userViewType = null,
            email = null,
            bio = null,
            twitterUsername = null
        )

        val userDetails = userDetailEntity.toUserDetails()

        assertEquals("testUser", userDetails.login)
        assertEquals(null, userDetails.id)
        assertEquals(null, userDetails.avatarUrl)
        assertEquals(null, userDetails.name)
        assertEquals(null, userDetails.company)
        assertEquals(null, userDetails.blog)
        assertEquals(null, userDetails.followers)
        assertEquals(null, userDetails.following)
        assertEquals(null, userDetails.notes) // Should remain null
    }
}