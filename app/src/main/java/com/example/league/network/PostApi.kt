package com.example.league.network

import com.example.league.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface PostApi {

    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>
}