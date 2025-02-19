package com.umtech.tawkandroid.data.remote

import com.umtech.tawkandroid.data.api.ApiService
import com.umtech.tawkandroid.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteDataSource(private val apiService: ApiService) {
    fun fetchUsers(since: Int): Flow<List<User>> = flow {
        emit(apiService.fetchUsers(since))
    }
}