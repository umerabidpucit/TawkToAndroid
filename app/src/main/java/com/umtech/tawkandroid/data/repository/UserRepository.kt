package com.umtech.tawkandroid.data.repository

import androidx.paging.PagingData
import com.umtech.tawkandroid.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    // Fetch users from API
    fun getUserData(since: Int): Flow<PagingData<User>>
}