package com.umtech.tawkandroid.presentation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.umtech.tawkandroid.presentation.viewmodel.MainViewModel
import com.umtech.tawkandroid.presentation.viewmodel.UserDetailViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun UserDetailsScreen(viewModel: UserDetailViewModel,
                      mainViewModel: MainViewModel,
                      username: String,
                      onBackClick: () -> Unit) {
    val userDetail by viewModel.userDetail.collectAsState()

    // Call API when screen loads
    LaunchedEffect(username) {
        viewModel.fetchUserDetails(username)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = username,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        userDetail?.let { user ->
            if (user.login == "User Not Found") {
                // ✅ Show Placeholder UI when User is Not Found
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "User Not Found",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                var notes by rememberSaveable { mutableStateOf(user.notes ?: "") }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    item {
                        // User Avatar
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.avatarUrl)
                                .crossfade(true) // ✅ Smooth transition
                                .diskCachePolicy(CachePolicy.ENABLED) // ✅ Cache images on disk
                                .memoryCachePolicy(CachePolicy.ENABLED) // ✅ Keep in memory for fast access
                                .build(),
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    item {
                        // Followers & Following Count
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = "Followers: ${user.followers}",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Following: ${user.following}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        // User Info Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(2.dp, Color.Black)
                                .padding(8.dp)
                        ) {
                            Column {
                                Text(text = "Name: ${user.name ?: "N/A"}")
                                Text(text = "Company: ${user.company ?: "N/A"}")
                                Text(text = "Blog: ${user.blog ?: "N/A"}")
                            }
                        }
                    }

                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = notes,
                                onValueChange = {
                                    if (it.length <= 250) notes = it
                                },
                                label = { Text("Notes") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 4
                            )

                            Text(
                                text = "${notes.length}/250 characters",
                                modifier = Modifier.align(Alignment.End),
                                color = Color.Gray
                            )

                            Button(
                                onClick = {
                                    user.login?.let { username ->
                                        viewModel.updateUserNotes(username, notes)
                                        mainViewModel.notifyNoteUpdated(username)
                                    }
                                    onBackClick() // Navigate back
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Save Notes")
                            }
                        }
                    }


                }
            }
        } ?: run {
            // ✅ Show Loading State
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading user details...", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}