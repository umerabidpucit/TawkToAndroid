package com.umtech.tawkandroid.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkMonitorTest {

    private lateinit var fakeNetworkMonitor: FakeNetworkMonitor

    @Before
    fun setup() {
        fakeNetworkMonitor = FakeNetworkMonitor()
    }

    @Test
    fun `emit true when network is available`() = runTest {
        fakeNetworkMonitor.setNetworkAvailable(true) // Simulate network available
        val result = fakeNetworkMonitor.observeNetworkState().first()
        assertEquals(true, result)
    }

    @Test
    fun `emit false when network is lost`() = runTest {
        fakeNetworkMonitor.setNetworkAvailable(false) // Simulate network lost
        val result = fakeNetworkMonitor.observeNetworkState().first()
        assertEquals(false, result)
    }
}

class FakeNetworkMonitor {
    private val _networkState = MutableStateFlow(false) // Default: No Internet

    fun setNetworkAvailable(isAvailable: Boolean) {
        _networkState.value = isAvailable
    }

    fun observeNetworkState(): Flow<Boolean> = _networkState
}