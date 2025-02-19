package com.umtech.tawkandroid.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.umtech.tawkandroid.data.model.User

@Composable
fun UserDetailsScreen(user: User) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Image banner as a header
        user.url?.let {
            item {
                Image(
                    painter = rememberImagePainter(it),
                    contentDescription = "Banner Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Posts in cells
        item {
            Text(text = "Posts:", modifier = Modifier.padding(start = 16.dp, end = 16.dp))
            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}