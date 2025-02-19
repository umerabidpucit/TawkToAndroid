package com.umtech.tawkandroid.data.repository

import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    // Fetch users from API
    fun getUserData(since: Int): Flow<List<User>>
}