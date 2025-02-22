package com.umtech.tawkandroid.data.repository

import androidx.paging.PagingData
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.model.UserDetails
import com.umtech.tawkandroid.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    // Fetch users from API
    fun getUserData(since: Int): Flow<PagingData<User>>
    fun getUserDetailData(username: String): Flow<UserDetails>
    fun searchUsers(query: String): Flow<List<UserEntity>>
}