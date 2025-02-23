package com.umtech.tawkandroid.data.remote

import com.umtech.tawkandroid.data.api.ApiService
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.model.UserDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import app.cash.turbine.test

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteDataSourceTest {

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var mockApiService: ApiService

    @Before
    fun setup() {
        mockApiService = mock()
        remoteDataSource = RemoteDataSource(mockApiService)
    }

    @Test
    fun `fetchUsers emits list of users`() = runTest {
        val mockUsers = listOf(
            User(login = "user1", id = 1, avatarUrl = "https://example.com/avatar1.png"),
            User(login = "user2", id = 2, avatarUrl = "https://example.com/avatar2.png")
        )

        `when`(mockApiService.fetchUsers(0)).thenReturn(mockUsers)

        remoteDataSource.fetchUsers(0).test {
            assertEquals(mockUsers, awaitItem()) // Expect emitted list to match mockUsers
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchUserDetail emits correct user details`() = runTest {
        val mockUserDetails = UserDetails(
            login = "user1",
            id = 1,
            avatarUrl = "https://example.com/avatar1.png",
            name = "Test User",
            bio = "A test user",
            followers = 10,
            following = 5
        )

        `when`(mockApiService.fetchUserDetail("user1")).thenReturn(mockUserDetails)

        remoteDataSource.fetchUserDetail("user1").test {
            assertEquals(mockUserDetails, awaitItem()) // Expect emitted details to match mockUserDetails
            cancelAndIgnoreRemainingEvents()
        }
    }
}