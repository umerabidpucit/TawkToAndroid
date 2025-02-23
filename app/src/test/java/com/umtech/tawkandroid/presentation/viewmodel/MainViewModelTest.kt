package com.umtech.tawkandroid.presentation.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.testIn
import com.umtech.tawkandroid.common.NetworkMonitor
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.model.UserEntity
import com.umtech.tawkandroid.domain.usecase.FetchUserUseCase
import com.umtech.tawkandroid.domain.usecase.SearchUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule() // Ensures coroutines run correctly

    private lateinit var viewModel: MainViewModel
    private val fetchUserUseCase: FetchUserUseCase = mockk(relaxed = true)
    private val searchUseCase: SearchUseCase = mockk()
    private val networkMonitor: NetworkMonitor = mockk()

    @Before
    fun setup() {
        every { networkMonitor.observeNetworkState() } returns flowOf(false) // Default offline
        viewModel = MainViewModel(fetchUserUseCase, searchUseCase, networkMonitor)
    }

    @Test
    fun `observeNetwork updates isConnected state`() = coroutineRule.runTest {
        val networkFlow = MutableStateFlow(false) // Initially offline
        every { networkMonitor.observeNetworkState() } returns networkFlow

        viewModel = MainViewModel(fetchUserUseCase, searchUseCase, networkMonitor)

        // Let the coroutine complete
        advanceUntilIdle()

        assertFalse(viewModel.isConnected.first()) // Should be false initially

        networkFlow.value = true // Simulate network becoming available

        // Wait for new value to be collected
        advanceUntilIdle()

        assertTrue(viewModel.isConnected.first()) // Should be true now
    }


    @Test
    fun `searchUsers updates searchResults`() = coroutineRule.runTest {
        val query = "John"
        val fakeResults = listOf(UserEntity(1, "John Doe", "url", "public"))
        val searchFlow = MutableStateFlow(fakeResults) // Use MutableStateFlow for emissions

        every { searchUseCase(query) } returns searchFlow

        viewModel.searchUsers(query)

        // Ensure coroutines complete
        advanceUntilIdle()

        assertEquals(fakeResults, viewModel.searchResults.value) // Use .value instead of first()
    }


    @Test
    fun `searchUsers returns empty list when no results found`() = coroutineRule.runTest {
        val query = "UnknownUser"
        every { searchUseCase(query) } returns flowOf(emptyList())

        viewModel.searchUsers(query)

        advanceUntilIdle()
        assertEquals(emptyList<UserEntity>(), viewModel.searchResults.value)
    }

    @Test
    fun `notifyNoteUpdated emits correct username`() = coroutineRule.runTest {
        val username = "johndoe"
        val turbine = viewModel.notesUpdated.testIn(this)

        viewModel.notifyNoteUpdated(username)

        assertEquals(username, turbine.awaitItem()) // Ensure username is emitted
        turbine.cancel()
    }

    @Test
    fun `users flow is initialized properly`() = coroutineRule.runTest {
        val pagingDataFlow = flowOf(PagingData.from(listOf(User("johndoe", 54))))
        every { fetchUserUseCase(any()) } returns pagingDataFlow

        viewModel = MainViewModel(fetchUserUseCase, searchUseCase, networkMonitor)

        assertNotNull(viewModel.users)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestWatcher() {

    private val testDispatcher = StandardTestDispatcher()

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher) // Set Main dispatcher for testing
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain() // Reset Main dispatcher after tests
    }

    fun runTest(block: suspend TestScope.() -> Unit) = kotlinx.coroutines.test.runTest {
        block()
    }
}

