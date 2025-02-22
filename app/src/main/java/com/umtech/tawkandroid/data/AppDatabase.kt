package com.umtech.tawkandroid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.umtech.tawkandroid.data.model.UserDetailEntity
import com.umtech.tawkandroid.data.model.UserEntity
import com.umtech.tawkandroid.data.repository.dao.UserDao
import com.umtech.tawkandroid.data.repository.dao.UserDetailDao

@Database(entities = [UserEntity::class, UserDetailEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun userDetailDao(): UserDetailDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // ✅ Clears database on schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}