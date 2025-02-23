package com.umtech.tawkandroid.common

import org.junit.jupiter.api.Assertions.*

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class NetworkUtilsTest {

    private lateinit var mockContext: Context
    private lateinit var mockConnectivityManager: ConnectivityManager

    @Before
    fun setup() {
        // Mock Context and ConnectivityManager
        mockContext = mock(Context::class.java)
        mockConnectivityManager = mock(ConnectivityManager::class.java)

        // Return mocked ConnectivityManager when requested
        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenReturn(mockConnectivityManager)
    }

    @Test
    fun `returns true when internet is available on Android M+`() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val mockNetwork = mock(Network::class.java)
            val mockCapabilities = mock(NetworkCapabilities::class.java)

            `when`(mockConnectivityManager.activeNetwork).thenReturn(mockNetwork)
            `when`(mockConnectivityManager.getNetworkCapabilities(mockNetwork))
                .thenReturn(mockCapabilities)
            `when`(mockCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                .thenReturn(true)

            val result = NetworkUtils.isInternetAvailable(mockContext)
            assertEquals(true, result)
        }
    }

    @Test
    fun `returns false when no active network on Android M+`() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            `when`(mockConnectivityManager.activeNetwork).thenReturn(null)

            val result = NetworkUtils.isInternetAvailable(mockContext)
            assertEquals(false, result)
        }
    }

    @Test
    fun `returns false when no internet capability on Android M+`() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val mockNetwork = mock(Network::class.java)
            val mockCapabilities = mock(NetworkCapabilities::class.java)

            `when`(mockConnectivityManager.activeNetwork).thenReturn(mockNetwork)
            `when`(mockConnectivityManager.getNetworkCapabilities(mockNetwork))
                .thenReturn(mockCapabilities)
            `when`(mockCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                .thenReturn(false)

            val result = NetworkUtils.isInternetAvailable(mockContext)
            assertEquals(false, result)
        }
    }

    @Test
    fun `returns true when internet is available on pre-Android M`() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val mockNetworkInfo = mock(NetworkInfo::class.java)

            `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(mockNetworkInfo)
            `when`(mockNetworkInfo.isConnected).thenReturn(true)

            val result = NetworkUtils.isInternetAvailable(mockContext)
            assertEquals(true, result)
        }
    }

    @Test
    fun `returns false when no internet on pre-Android M`() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(null)

            val result = NetworkUtils.isInternetAvailable(mockContext)
            assertEquals(false, result)
        }
    }
}