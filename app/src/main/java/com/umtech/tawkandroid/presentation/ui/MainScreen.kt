package com.umtech.tawkandroid.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.gson.Gson
import com.umtech.tawkandroid.R
import com.umtech.tawkandroid.presentation.viewmodel.MainViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = getViewModel()) {
    val users = viewModel.users.collectAsLazyPagingItems()
    val loadState = users.loadState

    when {
        loadState.refresh is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
        }

        loadState.refresh is LoadState.Error -> {
            ErrorMessage(loadState.refresh as LoadState.Error) { users.retry() }
        }

        users.itemCount == 0 -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_users_available),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        else -> {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(users.itemCount) { index ->
                    val user = users[index]
                    user?.let {
                        UserItem(
                            user = it,
                            index = index, // ✅ Pass index to UserItem
                            onClick = {
                                val userJson = Gson().toJson(it)
                                navController.navigate("user_details?user=$userJson")
                            }
                        )
                    }
                }

                // ✅ Show Loading Indicator When More Data is Being Loaded
                when (users.loadState.append) {
                    is LoadState.Loading -> item { LoadingIndicator() }
                    is LoadState.Error -> item { RetryButton { users.retry() } }
                    else -> Unit
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(36.dp))
    }
}

@Composable
fun RetryButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onClick) {
            Text(stringResource(R.string.retry))
        }
    }
}