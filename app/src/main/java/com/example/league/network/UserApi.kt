package com.example.league.network

import com.example.league.model.User
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {

    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}