package com.umtech.tawkandroid.data.repository

import com.umtech.tawkandroid.data.model.UserDetailEntity
import com.umtech.tawkandroid.data.model.UserDetails
import com.umtech.tawkandroid.data.model.UserEntity
import com.umtech.tawkandroid.data.model.toDetailEntity
import com.umtech.tawkandroid.data.remote.RemoteDataSource
import com.umtech.tawkandroid.data.repository.dao.UserDao
import com.umtech.tawkandroid.data.repository.dao.UserDetailDao
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    private lateinit var userDao: UserDao
    private lateinit var userDetailDao: UserDetailDao
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setUp() {
        // Initialize MockK
        userDao = mockk(relaxed = true)
        userDetailDao = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)

        // Initialize repository with mocks
        userRepository = UserRepositoryImpl(userDao, userDetailDao, remoteDataSource)
    }

    @Test
    fun `getUserData should return PagingData from PagingSource`() = runBlocking {
        val flow = userRepository.getUserData(0)
        assertNotNull(flow)
    }

    @Test
    fun `getUserDetailData should return cached user details when available`() = runTest {
        // Arrange
        val username = "test_user"

        // Use a real object instead of a mock
        val cachedUser = UserDetailEntity(
            login = username,
            name = "Test Name",
            avatarUrl = "https://example.com/avatar.png",
            id = null,
            type = null,
            userViewType = null,
            company = null,
            blog = null,
            email = null,
            bio = null,
            twitterUsername = null,
            followers = null,
            following = null,
            notes = null
        )

        coEvery { userDetailDao.getUserDetail(username) } returns cachedUser

        // Act
        val result = userRepository.getUserDetailData(username)
            .flowOn(StandardTestDispatcher(testScheduler)) // Ensures test runs on the test dispatcher
            .first()

        // Debugging
        println("Repository Output: $result")

        // Assert
        assertEquals(username, result.login)
        assertEquals("Test Name", result.name)

        // Verify that DAO was accessed, but network was not
        coVerify(exactly = 1) { userDetailDao.getUserDetail(username) }
        coVerify(exactly = 0) { remoteDataSource.fetchUserDetail(any()) }
    }

    @Test
    fun `getUserDetailData should fetch from API and store in DB when not cached`() = runBlocking {
        // Arrange
        val username = "test_user"
        val expectedUserDetails = UserDetails(
            login = username,
            id = 4,
            name = "Test User",
            company = "Test Company",
            blog = "https://test.blog",
            bio = "Test Bio",
            publicRepos = 10,
            followers = 50,
            following = 20
        )

        val expectedEntity = expectedUserDetails.toDetailEntity()

        // Mock DB: No cached user (returns null)
        coEvery { userDetailDao.getUserDetail(username) } returns null

        // Mock API: Returns UserDetails
        coEvery { remoteDataSource.fetchUserDetail(username) } returns flowOf(expectedUserDetails)

        // Mock DB: Insert UserDetails
        coEvery { userDetailDao.insertUserDetail(any()) } just Runs

        // Act
        val result = userRepository.getUserDetailData(username).first()

//        // Assert
//        assertThat(result).usingRecursiveComparison().isEqualTo(expectedUserDetails)

        // Verify DB fetch and API call
        coVerify(exactly = 1) { userDetailDao.getUserDetail(username) }
        coVerify(exactly = 1) { remoteDataSource.fetchUserDetail(username) }
        coVerify(exactly = 1) { userDetailDao.insertUserDetail(expectedEntity) }
    }


    @Test
    fun `getUserDetailData should return placeholder on API failure`() = runBlocking {
        val username = "test_user"

        coEvery { userDetailDao.getUserDetail(username) } returns null
        coEvery { remoteDataSource.fetchUserDetail(username) } throws Exception("API error")

        val result = userRepository.getUserDetailData(username).first()

        assertEquals(UserDetails.placeholder(), result)
    }

    @Test
    fun `searchUsers should return flow of user list`() = runBlocking {
        val query = "John"
        val users = listOf(mockk<UserEntity>(), mockk())
        coEvery { userDao.searchUsers(query) } returns flowOf(users)

        val result = userRepository.searchUsers(query).first()

        assertEquals(2, result.size)
    }
}
