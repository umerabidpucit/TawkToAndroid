package com.umtech.tawkandroid.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.umtech.tawkandroid.presentation.ui.MainScreen
import com.umtech.tawkandroid.presentation.ui.UserDetailsScreen
import com.umtech.tawkandroid.presentation.viewmodel.MainViewModel
import com.umtech.tawkandroid.presentation.viewmodel.UserDetailViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    // ✅ Get MainViewModel only once and share it across screens
    val mainViewModel: MainViewModel = getViewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Home Screen (Main Screen)
        composable("home") {
            MainScreen(
                navController = navController,
                viewModel = mainViewModel // ✅ Pass MainViewModel
            )
        }

        // User Details Screen
        composable("userDetail/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: return@composable

            // ✅ Get UserDetailViewModel using Koin
            val userDetailViewModel: UserDetailViewModel = getViewModel()

            UserDetailsScreen(
                viewModel = userDetailViewModel,
                mainViewModel = mainViewModel, // ✅ Pass MainViewModel
                username = username,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}