package com.umtech.tawkandroid.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.umtech.tawkandroid.data.model.UserDetailEntity
import com.umtech.tawkandroid.data.model.UserEntity
import com.umtech.tawkandroid.data.repository.dao.UserDao
import com.umtech.tawkandroid.data.repository.dao.UserDetailDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class) // ✅ Needed for running in test/
class AppDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var userDetailDao: UserDetailDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // ✅ Allows queries on the main thread for testing
            .build()
        userDao = database.userDao()
        userDetailDao = database.userDetailDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertUsersAndRetrieve() = runBlocking {
        val users = listOf(
            UserEntity(id = 1, login = "user1", avatarUrl = "url1", userViewType = "0", timestamp = 12345, hasNotes = false),
            UserEntity(id = 2, login = "user2", avatarUrl = "url2", userViewType = "1", timestamp = 67890, hasNotes = false)
        )
        userDao.insertUsers(users)

        val result = userDao.getUsersIds().first()
        assertEquals(2, result.size)
        assertTrue(result.contains(1))
        assertTrue(result.contains(2))
    }

    @Test
    fun updateUserNotes() = runBlocking {
        val user = UserEntity(id = 1, login = "user1", avatarUrl = "url1", userViewType = "0", timestamp = 12345, hasNotes = false)
        userDao.insertUsers(listOf(user))

        userDao.updateUserNotes("user1", true)

        val updatedUser = userDao.getUserByName("user1").first()
        assertNotNull(updatedUser)
        assertEquals(true, updatedUser?.hasNotes)
    }

    @Test
    fun deleteUser() = runBlocking {
        val user = UserEntity(id = 1, login = "user1", avatarUrl = "url1", userViewType = "0", timestamp = 12345, hasNotes = false)
        userDao.insertUsers(listOf(user))

        userDao.deleteUser(user)

        val count = userDao.getUserCount().first()
        assertEquals(0, count)
    }

    @Test
    fun insertUserDetailAndRetrieve() = runBlocking {
        val userDetail = UserDetailEntity(
            login = "user1", name = "John Doe", notes = "Test note",
            id = null,
            avatarUrl = null,
            type = null,
            userViewType = null,
            company = null,
            blog = null,
            email = null,
            bio = null,
            twitterUsername = null,
            followers = null,
            following = null
        )
        userDetailDao.insertUserDetail(userDetail)

        val retrievedDetail = userDetailDao.getUserDetail("user1")
        assertNotNull(retrievedDetail)
        assertEquals("John Doe", retrievedDetail?.name)
    }

    @Test
    fun updateNotes() = runBlocking {
        val userDetail = UserDetailEntity(
            login = "user1", name = "John Doe", notes = "",
            id = null,
            avatarUrl = null,
            type = null,
            userViewType = null,
            company = null,
            blog = null,
            email = null,
            bio = null,
            twitterUsername = null,
            followers = null,
            following = null
        )
        userDetailDao.insertUserDetail(userDetail)

        userDetailDao.updateNotes("user1", "Updated note")

        val updatedDetail = userDetailDao.getUserDetail("user1")
        assertEquals("Updated note", updatedDetail?.notes)
    }

    @Test
    fun checkHasNotes() = runBlocking {
        val userDetail = UserDetailEntity(
            login = "user1", name = "John Doe", notes = "Important note",
            id = null,
            avatarUrl = null,
            type = null,
            userViewType = null,
            company = null,
            blog = null,
            email = null,
            bio = null,
            twitterUsername = null,
            followers = null,
            following = null
        )
        userDetailDao.insertUserDetail(userDetail)

        val hasNotes = userDetailDao.hasNotes("user1")
        assertTrue(hasNotes)
    }
}
