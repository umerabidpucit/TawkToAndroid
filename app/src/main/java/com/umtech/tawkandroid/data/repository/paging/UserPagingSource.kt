package com.umtech.tawkandroid.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.model.toEntity
import com.umtech.tawkandroid.data.model.toUser
import com.umtech.tawkandroid.data.remote.RemoteDataSource
import com.umtech.tawkandroid.data.repository.dao.UserDao
import com.umtech.tawkandroid.data.repository.dao.UserDetailDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserPagingSource(
    private val userDao: UserDao,
    private val userDetailDao: UserDetailDao, // ✅ Added UserDetailDao for note lookup
    private val remoteDataSource: RemoteDataSource
) : PagingSource<Int, User>() {

    private val requestMutex = Mutex() // ✅ Prevents duplicate API calls

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return requestMutex.withLock {
            try {
                val lastStoredUserId = userDao.getMaxUserId().first() ?: 0
                val since = params.key ?: lastStoredUserId

                println("🔥 Fetching users from API → since: $since")

                // ✅ Step 1: Try Loading Users from Room
                val localUsers = userDao.getUsersPagingSource().load(params)

                if (localUsers is LoadResult.Page && localUsers.data.isNotEmpty()) {
                    val userList = localUsers.data.map { userEntity ->
                        userEntity.toUser()
                            .copy(hasNotes = userDetailDao.hasNotes(userEntity.login ?: ""))
                    }

                    // ✅ Use old working nextKey logic
                    val nextKey = if (localUsers.data.size < 30) null else since + 1

                    println("✅ Room Data Loaded → NextKey: $nextKey")

                    return LoadResult.Page(
                        data = userList,
                        prevKey = null,
                        nextKey = nextKey
                    )
                }

                // ✅ Step 2: Fetch new users from API
                val apiUsers = remoteDataSource.fetchUsers(since).first()

                if (apiUsers.isEmpty()) {
                    println("🚨 API Returned No Data. Stopping Pagination.")
                    return LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                }

                // ✅ Insert API Data into Room
                val newUsers = apiUsers.filter { (it.id ?: 0) > lastStoredUserId }
                if (newUsers.isNotEmpty()) {
                    userDao.insertUsers(newUsers.map { it.toEntity() })
                }

                // ✅ Fetch notes for API users
                val usersWithNotes = newUsers.map { user ->
                    user.copy(hasNotes = userDetailDao.hasNotes(user.login ?: ""))
                }

                // ✅ Use the old working nextKey logic
                val lastUserId = apiUsers.lastOrNull()?.id ?: return LoadResult.Page(
                    data = usersWithNotes,
                    prevKey = null,
                    nextKey = null
                )

                val nextKey = if (apiUsers.size < 30) null else lastUserId + 1

                println("✅ API Fetch Successful → NextKey: $nextKey")

                return LoadResult.Page(
                    data = usersWithNotes,
                    prevKey = null,
                    nextKey = nextKey
                )

            } catch (e: Exception) {
                println("🚨 Error in Paging: ${e.localizedMessage}")
                return LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.id
        }
    }
}