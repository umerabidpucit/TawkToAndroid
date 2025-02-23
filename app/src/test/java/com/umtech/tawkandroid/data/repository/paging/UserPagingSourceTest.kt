package com.umtech.tawkandroid.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.model.UserEntity
import com.umtech.tawkandroid.data.remote.RemoteDataSource
import com.umtech.tawkandroid.data.repository.dao.UserDao
import com.umtech.tawkandroid.data.repository.dao.UserDetailDao
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserPagingSourceTest {

    // Mock dependencies
    private val userDao: UserDao = mockk()
    private val userDetailDao: UserDetailDao = mockk()
    private val remoteDataSource: RemoteDataSource = mockk()

    // Paging source
    private lateinit var userPagingSource: UserPagingSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        clearMocks(userDao, userDetailDao, remoteDataSource)

        // ✅ Ensure correct type: PagingSource<Int, UserEntity>
        every { userDao.getUsersPagingSource() } returns object : PagingSource<Int, UserEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntity> {
                return LoadResult.Page(emptyList(), null, null)
            }

            override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? = null
        }

        userPagingSource = UserPagingSource(userDao, userDetailDao, remoteDataSource)
    }

    @Test
    fun `load returns page with users from API when local data is empty`() = runTest {
        // Arrange
        val fakeUsers = listOf(
            User(id = 1, login = "User1", avatarUrl = "https://example.com/user1.png"),
            User(id = 2, login = "User2", avatarUrl = "https://example.com/user2.png")
        )

        val fakeUserEntities = fakeUsers.map { user ->
            UserEntity(
                id = user.id,
                login = user.login,
                avatarUrl = user.avatarUrl,
                userViewType = null,
                timestamp = System.currentTimeMillis(),
                hasNotes = false
            )
        }

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0, loadSize = 2, placeholdersEnabled = false
        )

        // ✅ Mock database behavior (No local data)
        coEvery { userDao.getMaxUserId() } returns flowOf(0)

        // ✅ Mock remote API response
        coEvery { remoteDataSource.fetchUsers(0) } returns flowOf(fakeUsers)

        // ✅ Mock insertUsers() to avoid test failures
        coEvery { userDao.insertUsers(any()) } returns Unit

        // ✅ Mock userDetailDao.hasNotes()
        coEvery { userDetailDao.hasNotes("User1") } returns false
        coEvery { userDetailDao.hasNotes("User2") } returns true

        // Act
        val result = userPagingSource.load(loadParams)

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page

        assertEquals(2, page.data.size)
        assertEquals(fakeUserEntities[0].id, page.data[0].id)
        assertEquals(fakeUserEntities[0].login, page.data[0].login)
        assertEquals(false, page.data[0].hasNotes)

        assertEquals(fakeUserEntities[1].id, page.data[1].id)
        assertEquals(fakeUserEntities[1].login, page.data[1].login)
        assertEquals(true, page.data[1].hasNotes)

        assertEquals(null, page.prevKey)


        // ✅ Verify interactions
        coVerify(exactly = 1) { remoteDataSource.fetchUsers(0) }
        coVerify(exactly = 1) { userDao.insertUsers(any()) }
        coVerify(exactly = 1) { userDetailDao.hasNotes("User1") }
        coVerify(exactly = 1) { userDetailDao.hasNotes("User2") }
    }

    @Test
    fun `load returns error when API fails`() = runTest {
        // Arrange
        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0, loadSize = 2, placeholdersEnabled = false
        )

        // ✅ Mock getMaxUserId() so it doesn't fail before reaching fetchUsers()
        coEvery { userDao.getMaxUserId() } returns flowOf(0)

        // ✅ Mock getUsersPagingSource() with correct type: PagingSource<Int, UserEntity>
        every { userDao.getUsersPagingSource() } returns object : PagingSource<Int, UserEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntity> {
                return LoadResult.Page(emptyList(), null, null)
            }

            override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? = null
        }

        // Mock API failure
        coEvery { remoteDataSource.fetchUsers(0) } throws RuntimeException("API Error")

        // Act
        val result = userPagingSource.load(loadParams)

        // Assert
        assertTrue(result is PagingSource.LoadResult.Error)
        val errorResult = result as PagingSource.LoadResult.Error
        assertEquals("API Error", errorResult.throwable.message)

        // Verify API call
        coVerify(exactly = 1) { remoteDataSource.fetchUsers(0) }
    }
}