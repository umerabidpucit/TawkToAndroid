package com.umtech.tawkandroid.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.umtech.tawkandroid.R
import org.json.JSONObject

@Composable
fun ErrorMessage(loadState: LoadState.Error, onRetry: () -> Unit) {
    val errorMessage = remember(loadState.error) {
        try {
            val jsonError = JSONObject(loadState.error.localizedMessage ?: "{}")
            jsonError.optString("message", "")
        } catch (e: Exception) {
            ""
        }
    }

    val fallbackMessage = stringResource(R.string.something_went_wrong)
    val displayMessage = errorMessage.ifBlank { fallbackMessage }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = displayMessage, modifier = Modifier.padding(16.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}