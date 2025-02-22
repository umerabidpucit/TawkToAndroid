package com.umtech.tawkandroid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_details")
data class UserDetailEntity(
    @PrimaryKey val login: String,
    val id: Int?,
    val avatarUrl: String?,
    val type: String?,
    val userViewType: String?,
    val name: String?,
    val company: String?,
    val blog: String?,
    val email: String?,
    val bio: String?,
    val twitterUsername: String?,
    val followers: Int?,
    val following: Int?,
    val notes:String? = ""
)

fun UserDetailEntity.toUserDetails(): UserDetails {
    return UserDetails(
        login = this.login,
        id = this.id,
        avatarUrl = this.avatarUrl,
        name = this.name,
        company = this.company,
        blog = this.blog,
        followers = this.followers,
        following = this.following,
        notes = this.notes
    )
}
