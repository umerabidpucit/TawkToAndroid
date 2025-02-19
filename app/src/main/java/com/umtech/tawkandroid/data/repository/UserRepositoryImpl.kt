package com.umtech.tawkandroid.data.repository

import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

    // Fetch users from API, store in Room if empty
    override fun getUserData(since: Int): Flow<List<User>> = flow {

        val apiUsers = remoteDataSource.fetchUsers(since).firstOrNull()
        if (!apiUsers.isNullOrEmpty()) {
            emit(apiUsers) // Emit API data
        } else {
            emit(emptyList()) // Return empty list if no data
        }
    }
}