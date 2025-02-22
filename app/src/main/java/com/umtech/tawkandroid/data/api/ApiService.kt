package com.umtech.tawkandroid.data.api

import com.umtech.tawkandroid.data.model.User
import com.umtech.tawkandroid.data.model.UserDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun fetchUsers(@Query("since") since: Int): List<User>

    @GET("users/{username}")
    suspend fun fetchUserDetail(@Path("username") username: String): UserDetails
}