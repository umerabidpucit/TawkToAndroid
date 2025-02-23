package com.umtech.tawkandroid.domain.usecase

import com.umtech.tawkandroid.data.model.UserDetails
import com.umtech.tawkandroid.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.junit.Assert.assertTrue
import kotlin.test.DefaultAsserter.fail

@OptIn(ExperimentalCoroutinesApi::class)
class FetchUserDetailUseCaseTest {

    private lateinit var useCase: FetchUserDetailUseCase
    private val repository: UserRepository = mockk()

    @Before
    fun setup() {
        useCase = FetchUserDetailUseCase(repository)
    }

    @Test
    fun `invoke should return user details from repository`() = runTest {
        // Given: A username and mocked user details
        val username = "testUser"
        val mockUserDetails = UserDetails(
            login = "testUser",
            name = "Test User",
            notes = "Some notes"
        )

        // When: Repository returns a Flow of UserDetails
        coEvery { repository.getUserDetailData(username) } returns flowOf(mockUserDetails)

        // Act: Invoke the use case
        val result = useCase(username).first()

        // Then: Verify the emitted user details
        assertEquals(mockUserDetails, result)
    }

    @Test
    fun `invoke should return empty Flow when repository emits nothing`() = runTest {
        // Given: A username with no data in the repository
        val username = "unknownUser"

        // When: Repository returns an empty Flow
        coEvery { repository.getUserDetailData(username) } returns emptyFlow()

        // Act: Invoke the use case
        val result = useCase(username).toList()

        // Then: The result should be an empty list
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke should throw exception when repository call fails`() = runTest {
        // Given: A username that causes an error
        val username = "errorUser"

        // When: Repository throws an exception
        coEvery { repository.getUserDetailData(username) } throws RuntimeException("Network error")

        // Act & Assert: Expect an exception
        try {
            useCase(username).first()
            fail("Expected an exception but got none")
        } catch (e: Exception) {
            assertEquals("Network error", e.message)
        }
    }
}
