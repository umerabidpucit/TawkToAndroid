package com.umtech.tawkandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.remote.RemoteDataSource
import com.umtech.tawkandroid.data.repository.dao.UserDao
import com.umtech.tawkandroid.data.repository.paging.UserPagingSource
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

//    private val requestMutex =
//        Mutex() // Ensures only 1 network call at a time for each since(last count)

    // Fetch users from API, store in Room if empty
    override fun getUserData(since: Int): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, // ✅ Keep page size small but allow API to control batch size
                prefetchDistance = 1, // ✅ Only trigger API when user scrolls near end
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UserPagingSource(userDao, remoteDataSource) }
        ).flow
    }
}