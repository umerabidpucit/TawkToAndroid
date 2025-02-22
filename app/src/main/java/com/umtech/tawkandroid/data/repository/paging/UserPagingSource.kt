package com.umtech.tawkandroid.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.model.toEntity
import com.umtech.tawkandroid.data.model.toUser
import com.umtech.tawkandroid.data.remote.RemoteDataSource
import com.umtech.tawkandroid.data.repository.dao.UserDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserPagingSource(
    private val userDao: UserDao,
    private val remoteDataSource: RemoteDataSource
) : PagingSource<Int, User>() {

    private val requestMutex = Mutex() // ✅ Prevents duplicate API calls

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return requestMutex.withLock {
            try {
                // ✅ Get last saved user ID from Room (after app restart)
                val lastStoredUserId = userDao.getMaxUserId().first() ?: 0
                val since = params.key ?: lastStoredUserId // ✅ Start from last saved ID

                println("🔥 Fetching users from API → since: $since")

                // ✅ Load users from Room first
                val localUsers = userDao.getUsersPagingSource().load(params)

                if (localUsers is LoadResult.Page && localUsers.data.isNotEmpty()) {
                    val nextKey = if (localUsers.data.size < 30) null else since + 1
                    return LoadResult.Page(
                        data = localUsers.data.map { it.toUser() },
                        prevKey = null,
                        nextKey = nextKey
                    )
                }

                // ✅ Fetch new users from API
                val apiUsers = remoteDataSource.fetchUsers(since).first()

                if (apiUsers.isEmpty()) {
                    return LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null // ✅ Stop pagination if no new users
                    )
                }

                // ✅ Save users to Room
                val newUsers =
                    apiUsers.filter { (it.id ?: 0) > lastStoredUserId } // Avoid duplicates
                if (newUsers.isNotEmpty()) {
                    userDao.insertUsers(newUsers.map { it.toEntity() })
                }

                // ✅ Get the new last user ID
                val lastUserId = apiUsers.lastOrNull()?.id ?: return LoadResult.Page(
                    data = apiUsers,
                    prevKey = null,
                    nextKey = null
                )

                val nextKey = if (apiUsers.size < 30) null else lastUserId + 1 // ✅ Correct next key

                println("✅ API Fetch Successful → NextKey: $nextKey")

                return LoadResult.Page(
                    data = apiUsers,
                    prevKey = null,
                    nextKey = nextKey
                )

            } catch (e: Exception) {
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