package com.umtech.tawkandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.model.UserDetails
import com.umtech.tawkandroid.data.model.UserEntity
import com.umtech.tawkandroid.data.model.toDetailEntity
import com.umtech.tawkandroid.data.model.toUserDetails
import com.umtech.tawkandroid.data.remote.RemoteDataSource
import com.umtech.tawkandroid.data.repository.dao.UserDao
import com.umtech.tawkandroid.data.repository.dao.UserDetailDao
import com.umtech.tawkandroid.data.repository.paging.UserPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val userDetailDao: UserDetailDao,
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

    // Fetch users from API, store in Room if empty
    override fun getUserData(since: Int): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, // ✅ Fetch in batches of 20
                prefetchDistance = 1, // ✅ Start fetching earlier to avoid lag
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                UserPagingSource(
                    userDao,
                    userDetailDao,
                    remoteDataSource
                )
            }
        ).flow
    }

    override fun getUserDetailData(username: String): Flow<UserDetails> = flow {

        try {
            // Check if user details exist in Room
            val cachedUser = userDetailDao.getUserDetail(username)

            if (cachedUser != null) {
                emit(cachedUser.toUserDetails()) // ✅ Convert Entity to Domain Model
            } else {
                // Fetch from API
                val userDetails: UserDetails =
                    remoteDataSource.fetchUserDetail(username).first() // ✅ Explicit Type

                // Convert API response to Room Entity
                val userDetailEntity = userDetails.toDetailEntity()

                // Insert into Room Database
                userDetailDao.insertUserDetail(userDetailEntity)

                // ✅ Ensure `emit()` receives the correct type
                emit(userDetails)
            }
        } catch (e: Exception) {
            emit(UserDetails.placeholder()) // ✅ Show placeholder if API fails
        }
    }.flowOn(Dispatchers.IO)

    override fun searchUsers(query: String): Flow<List<UserEntity>> = userDao.searchUsers(query)
}