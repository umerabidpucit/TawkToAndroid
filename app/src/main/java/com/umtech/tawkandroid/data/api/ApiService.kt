package com.umtech.tawkandroid.data.api

import com.umtech.tawkandroid.data.model.User
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun fetchUsers(@Query("since") since: Int): List<User>
}