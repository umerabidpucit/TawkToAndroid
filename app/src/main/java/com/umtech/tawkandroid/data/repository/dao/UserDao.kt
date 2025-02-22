package com.umtech.tawkandroid.data.repository.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umtech.tawkandroid.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("UPDATE users SET hasNotes = :hasNotes WHERE login = :username")
    suspend fun updateUserNotes(
        username: String,
        hasNotes: Boolean
    ) // Might utilise this for Notes icon display issue

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>) // Using to insert data into table

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsersPagingSource(): PagingSource<Int, UserEntity> // Initially created for load Data but now I am using getUsersWithNotes

    @Query("SELECT id FROM users")
    fun getUsersIds(): Flow<List<Int>> // Not required anymore just added to check duplicate records

    @Query("SELECT * FROM users WHERE login = :username")
    fun getUserByName(username: String): Flow<UserEntity> // Might utilise this for Notes icon display issue

    @Delete
    suspend fun deleteUser(user: UserEntity) // Just for testing purpose

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int> // Just created from testing

    @Query("SELECT MAX(id) FROM users")
    fun getMaxUserId(): Flow<Int?> // To get nextKeyValue to pass to the url

    @Query(
        """
    SELECT u.id, u.login, u.avatarUrl, u.userViewType, u.timestamp, 
           CASE 
               WHEN ud.notes IS NOT NULL AND ud.notes != '' THEN 1 
               ELSE 0 
           END AS hasNotes
    FROM users u
    LEFT JOIN user_details ud ON u.login = ud.login
    ORDER BY u.id ASC
    """
    )
    fun getUsersWithNotes(): PagingSource<Int, UserEntity>

    @Query("""
    SELECT u.* FROM users u
    LEFT JOIN user_details ud ON u.login = ud.login
    WHERE u.login GLOB '*' || :query || '*'
    OR (ud.notes GLOB '*' || :query || '*' AND ud.login IS NOT NULL)
""")
    fun searchUsers(query: String): Flow<List<UserEntity>>
}