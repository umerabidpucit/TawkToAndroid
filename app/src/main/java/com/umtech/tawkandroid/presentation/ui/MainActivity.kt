package com.umtech.tawkandroid.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.umtech.tawkandroid.presentation.ui.navigation.AppNavHost
import com.umtech.tawkandroid.presentation.ui.theme.KoinMVIComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinMVIComposeTheme {
                // Set up a Surface for theming and background
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Create the NavController for navigation
                    val navController = rememberNavController()

                    // Provide the NavController to the AppNavHost composable
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}