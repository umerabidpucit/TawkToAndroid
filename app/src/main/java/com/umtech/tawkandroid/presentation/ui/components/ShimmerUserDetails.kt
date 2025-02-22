package com.umtech.tawkandroid.presentation.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerUserDetails() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Back Button Placeholder
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .height(32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Avatar Placeholder
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Followers & Following Placeholder
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .size(80.dp, 20.dp)
            )
            ShimmerEffect(
                modifier = Modifier
                    .size(80.dp, 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Info Placeholder
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Notes Placeholder
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}