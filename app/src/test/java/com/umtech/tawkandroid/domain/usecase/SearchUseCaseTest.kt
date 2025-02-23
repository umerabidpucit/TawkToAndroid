package com.umtech.tawkandroid.domain.usecase

import com.umtech.tawkandroid.data.model.UserEntity
import com.umtech.tawkandroid.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchUseCaseTest {

    private lateinit var useCase: SearchUseCase
    private val repository: UserRepository = mockk()

    @Before
    fun setup() {
        useCase = SearchUseCase(repository)
    }

    @Test
    fun `invoke should return matching users from repository`() = runTest {
        // Given: Mocked search query and users
        val query = "john"
        val mockUsers = listOf(
            UserEntity(
                id = 1,
                login = "john_doe",
                avatarUrl = "url1",
                userViewType = "0",
                timestamp = 12345,
                hasNotes = false
            ),
            UserEntity(
                id = 2,
                login = "john_smith",
                avatarUrl = "url2",
                userViewType = "1",
                timestamp = 67890,
                hasNotes = false
            )
        )

        // When: Repository returns a Flow of matching users
        coEvery { repository.searchUsers(query) } returns flowOf(mockUsers)

        // Act: Invoke the use case
        val result = useCase(query).first()

        // Then: Verify the returned list matches expected users
        assertEquals(2, result.size)
        assertEquals("john_doe", result[0].login)
        assertEquals("john_smith", result[1].login)
    }

    @Test
    fun `invoke should return empty list when no users match query`() = runTest {
        // Given: A search query with no matching users
        val query = "xyz"

        // When: Repository returns an empty Flow
        coEvery { repository.searchUsers(query) } returns flowOf(emptyList())

        // Act: Invoke the use case
        val result = useCase(query).first()

        // Then: Verify the result is empty
        assertEquals(0, result.size)
    }

    @Test
    fun `invoke should handle repository exceptions`() = runTest {
        // Given: A search query
        val query = "error_case"

        // When: Repository throws an exception
        coEvery { repository.searchUsers(query) } throws RuntimeException("Database error")

        // Act & Assert: Expect an exception
        try {
            useCase(query).first()
            assertEquals("Expected an exception but got none", false)
        } catch (e: Exception) {
            assertEquals("Database error", e.message)
        }
    }
}
