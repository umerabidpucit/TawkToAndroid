package com.umtech.tawkandroid.presentation.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerEffect(modifier: Modifier = Modifier) {
    val gradientWidth = 300f // Width of shimmer effect
    val shimmerOffset = remember { Animatable(-gradientWidth) }

    // Start shimmer animation
    LaunchedEffect(Unit) {
        shimmerOffset.animateTo(
            targetValue = 1000f, // Moves shimmer to the right
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.4f),
            Color.Gray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.4f)
        ),
        start = Offset(shimmerOffset.value, 0f),
        end = Offset(shimmerOffset.value + gradientWidth, 0f)
    )

    Box(
        modifier = modifier
            .background(shimmerBrush)
            .clip(RoundedCornerShape(8.dp))
    )
}