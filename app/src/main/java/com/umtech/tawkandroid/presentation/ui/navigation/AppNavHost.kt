package com.umtech.tawkandroid.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.presentation.ui.MainScreen
import com.umtech.tawkandroid.presentation.ui.UserDetailsScreen
import com.umtech.tawkandroid.presentation.viewmodel.UserDetailViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Home Screen (your initial screen where you list users)
        composable("home") {
            MainScreen(navController = navController)
        }

        // User Details Screen
        composable("userDetail/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: return@composable

            // ✅ Get ViewModel using Koin
            val viewModel: UserDetailViewModel = getViewModel()

            UserDetailsScreen(
                viewModel = viewModel,
                username = username,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}