package com.umtech.tawkandroid.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umtech.tawkandroid.data.model.User

@Composable
fun UserItem(user: User, index: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Apply color inversion filter if index % 4 == 3
            val colorFilter = if (index % 4 == 3) {
                androidx.compose.ui.graphics.ColorFilter.colorMatrix(getInvertedColorMatrix())
            } else null

            Box(
                modifier = Modifier
                    .size(70.dp) // ✅ Outer circle size
                    .clip(CircleShape)
                    .background(androidx.compose.ui.graphics.Color.Black), // ✅ Black outline (border)
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp) // ✅ Inner circle (background area)
                        .clip(CircleShape)
                        .background(androidx.compose.ui.graphics.Color.White), // ✅ Background inside the border
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = "Thumbnail for ${user.login}",
                        modifier = Modifier
                            .size(64.dp) // ✅ Image should be smaller to fit inside
                            .clip(CircleShape),
                        colorFilter = if (index % 4 == 3) {
                            androidx.compose.ui.graphics.ColorFilter.colorMatrix(
                                getInvertedColorMatrix()
                            )
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                user.login?.let { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
                Text(
                    text = "Posts: ${user.userViewType}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Forward",
                modifier = Modifier.size(24.dp)
            )

            if (user.hasNotes) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Has Notes",
                    tint = Color.Yellow,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

fun getInvertedColorMatrix(): androidx.compose.ui.graphics.ColorMatrix {
    return androidx.compose.ui.graphics.ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,  // Invert Red
            0f, -1f, 0f, 0f, 255f,  // Invert Green
            0f, 0f, -1f, 0f, 255f,  // Invert Blue
            0f, 0f, 0f, 1f, 0f   // Preserve Alpha
        )
    )
}


@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    UserItem(
        user = User(
            url = "url"
        ),
        index = 4,
        onClick = { /* Do nothing for preview */ }

    )

}