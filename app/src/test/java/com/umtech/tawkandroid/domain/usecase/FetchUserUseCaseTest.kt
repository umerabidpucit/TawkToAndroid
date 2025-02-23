package com.umtech.tawkandroid.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.map
import androidx.paging.testing.asSnapshot
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FetchUserUseCaseTest {

    private lateinit var useCase: FetchUserUseCase
    private val repository: UserRepository = mockk()

    @Before
    fun setup() {
        useCase = FetchUserUseCase(repository)
    }

    @Test
    fun `invoke should return user list from repository`() = runTest {
        // Given: Mocked users list
        val since = 0
        val mockUsers = listOf(
            User(id = 1, login = "user1", avatarUrl = "url1"),
            User(id = 2, login = "user2", avatarUrl = "url2")
        )

        // Use Pager to create PagingData from FakeUserPagingSource
        val pager = Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { FakeUserPagingSource(mockUsers) }
        )

        // When: Repository returns a Flow of PagingData
        coEvery { repository.getUserData(since) } returns flowOf(pager.flow.first())

        // Act: Convert PagingData to list using `.asSnapshot()`
        val result = useCase(since).asSnapshot()

        // Then: Verify emitted PagingData contains expected users
        assertEquals(2, result.size)  // ✅ Now works!
        assertEquals("user1", result[0].login)  // ✅ Now works!
    }

    @Test
    fun `invoke should return empty PagingData when repository has no data`() = runTest {
        // Given: No users available
        val since = 0
        val emptyPagingData = PagingData.from(emptyList<User>())

        // When: Repository returns an empty PagingData flow
        coEvery { repository.getUserData(since) } returns flowOf(emptyPagingData)

        // Act: Collect emitted PagingData
        val result = useCase(since).collectData()

        // Then: Verify result is empty
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke should throw exception when repository call fails`() = runTest {
        // Given: A failing repository call
        val since = 0
        coEvery { repository.getUserData(since) } throws RuntimeException("Network error")

        // Act & Assert: Expect an exception
        try {
            useCase(since).collectData()
            assertTrue("Expected an exception but got none", false)
        } catch (e: Exception) {
            assertEquals("Network error", e.message)
        }
    }


    private suspend fun <T : Any> Flow<PagingData<T>>.collectData(): List<T> {
        val items = mutableListOf<T>()
        collect { pagingData ->
            pagingData.map { items.add(it) }
        }
        return items
    }
}


class FakeUserPagingSource(private val users: List<User>) : PagingSource<Int, User>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return LoadResult.Page(
            data = users,
            prevKey = null, // No previous page
            nextKey = null  // No next page (single page test)
        )
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? = null
}


