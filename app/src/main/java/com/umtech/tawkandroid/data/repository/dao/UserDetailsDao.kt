package com.umtech.tawkandroid.data.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umtech.tawkandroid.data.model.UserDetailEntity

@Dao
interface UserDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetail(userDetail: UserDetailEntity) // ✅ Accepts single entity

    @Query("SELECT * FROM user_details WHERE login = :username LIMIT 1")
    suspend fun getUserDetail(username: String): UserDetailEntity? // ✅ Returns single entity

    @Query("UPDATE user_details SET notes = :notes WHERE login = :username")
    suspend fun updateNotes(username: String, notes: String)
}