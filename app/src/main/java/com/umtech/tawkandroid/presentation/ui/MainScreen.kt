package com.umtech.tawkandroid.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.umtech.tawkandroid.R
import com.umtech.tawkandroid.common.NetworkUtils.isInternetAvailable
import com.umtech.tawkandroid.data.model.toUser
import com.umtech.tawkandroid.presentation.viewmodel.MainViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = getViewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val users = viewModel.users.collectAsLazyPagingItems()
    val searchResults by viewModel.searchResults.collectAsState()

    val isConnected by viewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        if (isConnected) {
            if (users.itemCount == 0) {
                users.retry()
            }
        }
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.notesUpdated.collect { updatedUsername ->
            val index = users.itemSnapshotList.indexOfFirst { it?.login == updatedUsername }
            if (index != -1) {
                users[index]?.hasNotes = true
                users.refresh()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.searchUsers(it)
            },
            placeholder = { Text("Search users...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        when {
            searchQuery.isNotEmpty() -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(searchResults) { index, userEntity ->
                        userEntity?.let {
                            UserItem(
                                user = it.toUser(),
                                index = index,
                                onClick = { navController.navigate("userDetail/${it.login}") }
                            )
                        }
                    }
                }
            }

            users.loadState.refresh is LoadState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                }
            }

            users.loadState.refresh is LoadState.Error -> {
                ErrorMessage(users.loadState.refresh as LoadState.Error) {
                    if (isInternetAvailable(context)) {
                        users.retry()
                    }
                }
            }

            users.itemCount == 0 -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.no_users_available),
                            modifier = Modifier.padding(16.dp)
                        )
                        if (isInternetAvailable(context)) {
                            RetryButton { users.retry() }
                        } else {
                            Text(text = stringResource(R.string.no_internet))
                        }
                    }
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