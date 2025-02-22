package com.umtech.tawkandroid.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.umtech.tawkandroid.R
import com.umtech.tawkandroid.presentation.viewmodel.MainViewModel
import okhttp3.internal.notify
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = getViewModel()) {
    val users = viewModel.users.collectAsLazyPagingItems()

    // ✅ Listen for note updates and refresh only the affected user
    LaunchedEffect(Unit) {
        viewModel.notesUpdated.collect { updatedUsername ->
            println("✅ Received update for user: $updatedUsername") // Debugging log

            val index = users.itemSnapshotList.indexOfFirst { it?.login == updatedUsername }
            if (index != -1) {
                println("🔄 Refreshing user at index: $index") // Debugging log

                users.refresh()
            } else {
                println("⚠️ User not found in list!") // Debugging log
            }
        }
    }

    when {
        users.loadState.refresh is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
        }

        users.loadState.refresh is LoadState.Error -> {
            ErrorMessage(users.loadState.refresh as LoadState.Error) { users.retry() }
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
                itemsIndexed(users.itemSnapshotList) { index, user ->
                    user?.let {
                        UserItem(
                            user = it,
                            index = index,
                            onClick = {
                                navController.navigate("userDetail/${user.login}")
                            }
                        )
                    }
                }

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