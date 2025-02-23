package com.umtech.tawkandroid.data.model

import android.os.Parcel
import kotlinx.parcelize.parcelableCreator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserDetailsTest {
    @Test
    fun `UserDetails model initializes correctly`() {
        val userDetails = UserDetails(
            login = "testUser",
            id = 123,
            avatarUrl = "https://example.com/avatar.png",
            name = "Test User",
            company = "Tech Corp",
            email = "test@example.com",
            followers = 100,
            following = 50,
            bio = "Software Developer",
            notes = "Important user"
        )

        assertEquals("testUser", userDetails.login)
        assertEquals(123, userDetails.id)
        assertEquals("https://example.com/avatar.png", userDetails.avatarUrl)
        assertEquals("Test User", userDetails.name)
        assertEquals("Tech Corp", userDetails.company)
        assertEquals("test@example.com", userDetails.email)
        assertEquals(100, userDetails.followers)
        assertEquals(50, userDetails.following)
        assertEquals("Software Developer", userDetails.bio)
        assertEquals("Important user", userDetails.notes)
    }

    @Test
    fun `UserDetails model default values are correct`() {
        val userDetails = UserDetails()

        assertEquals(null, userDetails.login)
        assertEquals(null, userDetails.id)
        assertEquals(null, userDetails.avatarUrl)
        assertEquals(null, userDetails.name)
        assertEquals(null, userDetails.company)
        assertEquals(null, userDetails.email)
        assertEquals(null, userDetails.followers)
        assertEquals(null, userDetails.following)
        assertEquals(null, userDetails.bio)
        assertEquals("", userDetails.notes) // Default value should be an empty string
    }

    @Test
    fun `UserDetails model is Parcelable`() {
        val userDetails = UserDetails(
            login = "testUser",
            id = 123,
            avatarUrl = "https://example.com/avatar.png",
            name = "Test User",
            company = "Tech Corp"
        )

        val parcel = Parcel.obtain()
        userDetails.writeToParcel(parcel, 0)
        parcel.setDataPosition(0) // Reset position for reading

        val createdFromParcel = parcel.readParcelable<UserDetails>(UserDetails::class.java.classLoader)
        assertNotNull(createdFromParcel)
        assertEquals(userDetails.login, createdFromParcel?.login)
        assertEquals(userDetails.id, createdFromParcel?.id)
        assertEquals(userDetails.avatarUrl, createdFromParcel?.avatarUrl)
        assertEquals(userDetails.name, createdFromParcel?.name)
        assertEquals(userDetails.company, createdFromParcel?.company)

        parcel.recycle()
    }

    @Test
    fun `UserDetails placeholder returns default values`() {
        val placeholder = UserDetails.placeholder()

        assertEquals("User Not Found", placeholder.login)
        assertEquals(-1, placeholder.id)
        assertEquals("", placeholder.avatarUrl)
        assertEquals("Unknown", placeholder.name)
        assertEquals("N/A", placeholder.company)
        assertEquals("N/A", placeholder.blog)
        assertEquals("N/A", placeholder.email)
        assertEquals("No bio available", placeholder.bio)
        assertEquals("N/A", placeholder.twitterUsername)
        assertEquals(0, placeholder.followers)
        assertEquals(0, placeholder.following)
        assertEquals("N/A", placeholder.type)
        assertEquals("N/A", placeholder.userViewType)
    }

    @Test
    fun `toDetailEntity converts UserDetails to UserDetailEntity correctly`() {
        val userDetails = UserDetails(
            login = "testUser",
            id = 123,
            avatarUrl = "https://example.com/avatar.png",
            name = "Test User",
            followers = 100,
            following = 50,
            company = "Tech Corp",
            blog = "https://blog.example.com",
            notes = "Important user",
            email = "test@example.com",
            bio = "Software Developer",
            twitterUsername = "testTwitter"
        )

        val entity = userDetails.toDetailEntity()

        assertEquals("testUser", entity.login)
        assertEquals(123, entity.id)
        assertEquals("https://example.com/avatar.png", entity.avatarUrl)
        assertEquals("Test User", entity.name)
        assertEquals(100, entity.followers)
        assertEquals(50, entity.following)
        assertEquals("Tech Corp", entity.company)
        assertEquals("https://blog.example.com", entity.blog)
        assertEquals("Important user", entity.notes)
        assertEquals("test@example.com", entity.email)
        assertEquals("Software Developer", entity.bio)
        assertEquals("testTwitter", entity.twitterUsername)
    }

    @Test
    fun `toDetailEntity handles null values correctly`() {
        val userDetails = UserDetails()

        val entity = userDetails.toDetailEntity()

        assertEquals("null", entity.login) // `toString()` on null returns "null"
        assertEquals(null, entity.id)
        assertEquals(null, entity.avatarUrl)
        assertEquals(null, entity.name)
        assertEquals(null, entity.followers)
        assertEquals(null, entity.following)
        assertEquals(null, entity.company)
        assertEquals(null, entity.blog)
        assertEquals("", entity.notes) // Default value should be an empty string
        assertEquals(null, entity.email)
        assertEquals(null, entity.bio)
        assertEquals(null, entity.twitterUsername)
    }
}