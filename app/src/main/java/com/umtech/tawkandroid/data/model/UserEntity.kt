package com.umtech.tawkandroid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int?,
    val login: String?,
    val avatarUrl: String?,
    val userViewType: String?,
    val timestamp: Long = System.currentTimeMillis() // Track order on basis of timestamp
)

fun UserEntity.toUser(): User {
    return User(
        id = this.id,
        login = this.login,
        avatarUrl = this.avatarUrl,
        userViewType = this.userViewType
    )
}