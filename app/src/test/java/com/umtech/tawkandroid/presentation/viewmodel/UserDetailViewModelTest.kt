package com.umtech.tawkandroid.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.umtech.tawkandroid.data.model.UserDetails
import com.umtech.tawkandroid.data.model.UserDetailEntity
import com.umtech.tawkandroid.data.repository.dao.UserDetailDao
import com.umtech.tawkandroid.domain.usecase.FetchUserDetailUseCase
import com.umtech.tawkandroid.presentation.viewmodel.UserDetailViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {

    // Required to make LiveData execute immediately
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Coroutine test dispatcher
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: UserDetailViewModel
    private val fetchUserDetailUseCase: FetchUserDetailUseCase = mockk()
    private val userDetailsDao: UserDetailDao = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserDetailViewModel(fetchUserDetailUseCase, userDetailsDao)
    }

    @Test
    fun `fetchUserDetails should load user from database if exists`() = runTest {
        // Given: A user exists in the database
        val username = "test_user"
        val userDetailEntity = UserDetailEntity(
            login = username, name = "Test User", notes = "Some notes",
            id = null,
            avatarUrl = null,
            type = null,
            userViewType = null,
            company = null,
            blog = null,
            email = null,
            bio = null,
            twitterUsername = null,
            followers = null,
            following = null
        )

        coEvery { userDetailsDao.getUserDetail(username) } returns userDetailEntity

        // When: Fetch user details is called
        viewModel.fetchUserDetails(username)

        // Move the coroutine dispatcher forward
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The user detail should be updated
        assertEquals(username, viewModel.userDetail.value?.login)
        assertEquals("Test User", viewModel.userDetail.value?.name)
    }

    @Test
    fun `fetchUserDetails should fetch user from API if not in database`() = runTest {
        // Given: A user is NOT in the database but exists in API
        val username = "new_user"
        val userDetails = UserDetails(login = username, name = "New User", notes = "New Note")

        coEvery { userDetailsDao.getUserDetail(username) } returns null
        coEvery { fetchUserDetailUseCase(username) } returns flowOf(userDetails)
        coEvery { userDetailsDao.insertUserDetail(any()) } just Runs

        // When: Fetch user details is called
        viewModel.fetchUserDetails(username)

        // Move the coroutine dispatcher forward
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The user detail should be updated
        assertEquals(username, viewModel.userDetail.value?.login)
        assertEquals("New User", viewModel.userDetail.value?.name)
        coVerify { userDetailsDao.insertUserDetail(any()) }
    }

    @Test
    fun `updateUserNotes should update notes in database`() = runTest {
        // Given: A user and new notes
        val username = "test_user"
        val newNotes = "Updated Notes"

        coEvery { userDetailsDao.updateNotes(username, newNotes) } just Runs

        // When: Update notes is called
        viewModel.updateUserNotes(username, newNotes)

        // Move the coroutine dispatcher forward
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Verify that the DAO update method was called
        coVerify { userDetailsDao.updateNotes(username, newNotes) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
