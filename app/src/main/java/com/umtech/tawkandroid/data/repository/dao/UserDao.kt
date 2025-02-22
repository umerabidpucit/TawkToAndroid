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
    suspend fun updateUserNotes(username: String, hasNotes: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsersPagingSource(): PagingSource<Int, UserEntity>

    @Query("SELECT id FROM users") // ✅ Fetch only IDs to filter duplicates
    fun getUsersIds(): Flow<List<Int>>

    @Query("SELECT * FROM users WHERE login = :username")
    fun getUserByName(username: String): Flow<UserEntity>

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>

    @Query("SELECT MAX(id) FROM users")
    fun getMaxUserId(): Flow<Int?>

    @Query("""
    SELECT u.id, u.login, u.avatarUrl, u.userViewType, u.timestamp, 
           CASE 
               WHEN ud.notes IS NOT NULL AND ud.notes != '' THEN 1 
               ELSE 0 
           END AS hasNotes
    FROM users u
    LEFT JOIN user_details ud ON u.login = ud.login
    ORDER BY u.id ASC
""")
    fun getUsersWithNotes(): PagingSource<Int, UserEntity>
}