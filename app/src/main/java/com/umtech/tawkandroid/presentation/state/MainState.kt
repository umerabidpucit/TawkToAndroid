package com.umtech.tawkandroid.presentation.state

import androidx.paging.PagingData
import com.umtech.tawkandroid.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MainState(
    val users: Flow<PagingData<User>> = emptyFlow(),
    val isLoading: Boolean = false,
    val error: String? = null
)