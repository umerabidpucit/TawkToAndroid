package com.umtech.tawkandroid.presentation.state

import com.umtech.tawkandroid.data.model.User

data class MainState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<User> = emptyList()
)