package com.umtech.tawkandroid.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserDetails(
    @SerializedName("login") var login: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("node_id") var nodeId: String? = null,
    @SerializedName("avatar_url") var avatarUrl: String? = null,
    @SerializedName("gravatar_id") var gravatarId: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("html_url") var htmlUrl: String? = null,
    @SerializedName("followers_url") var followersUrl: String? = null,
    @SerializedName("following_url") var followingUrl: String? = null,
    @SerializedName("gists_url") var gistsUrl: String? = null,
    @SerializedName("starred_url") var starredUrl: String? = null,
    @SerializedName("subscriptions_url") var subscriptionsUrl: String? = null,
    @SerializedName("organizations_url") var organizationsUrl: String? = null,
    @SerializedName("repos_url") var reposUrl: String? = null,
    @SerializedName("events_url") var eventsUrl: String? = null,
    @SerializedName("received_events_url") var receivedEventsUrl: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("user_view_type") var userViewType: String? = null,
    @SerializedName("site_admin") var siteAdmin: Boolean? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("company") var company: String? = null,
    @SerializedName("blog") var blog: String? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("hireable") var hireable: Boolean? = null,
    @SerializedName("bio") var bio: String? = null,
    @SerializedName("twitter_username") var twitterUsername: String? = null,
    @SerializedName("public_repos") var publicRepos: Int? = null,
    @SerializedName("public_gists") var publicGists: Int? = null,
    @SerializedName("followers") var followers: Int? = null,
    @SerializedName("following") var following: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    var notes: String? = ""

) : Parcelable {
    companion object {
        fun placeholder(): UserDetails {
            return UserDetails(
                login = "User Not Found",
                id = -1,
                avatarUrl = "",
                name = "Unknown",
                company = "N/A",
                blog = "N/A",
                email = "N/A",
                bio = "No bio available",
                twitterUsername = "N/A",
                followers = 0,
                following = 0,
                type = "N/A",
                userViewType = "N/A"
            )
        }
    }
}

fun UserDetails.toDetailEntity(): UserDetailEntity {
    return UserDetailEntity(
        login = this.login.toString(),
        name = this.name,
        avatarUrl = this.avatarUrl,
        followers = this.followers,
        following = this.following,
        company = this.company,
        blog = this.blog,
        notes = this.notes,
        id = this.id,
        type = this.type,
        userViewType = this.userViewType,
        email = this.email,
        bio = this.bio,
        twitterUsername = this.twitterUsername // Default notes field
    )
}

