package com.example.league.repository

import com.example.league.model.Post
import com.example.league.network.NetworkResult
import retrofit2.http.GET

interface PostRepo {

    @GET("posts")
    suspend fun getPosts(): NetworkResult<List<Post>>
}