package com.example.league.repository

import com.example.league.model.Post
import com.example.league.network.NetworkResult
import com.example.league.network.PostApi
import com.example.league.network.RetrofitClient.getRetrofit
import com.example.league.network.getErrorMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class PostRepoImpl: PostRepo {

    private val api = getRetrofit().create(PostApi::class.java)

    override suspend fun getPosts(): NetworkResult<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPosts()
                if (response.isSuccessful) {
                    response.body() ?. let {
                        NetworkResult.Success(data = it)
                    } ?:  NetworkResult.Error("get post body is empty")
                } else {
                    NetworkResult.Error(getErrorMessages(response.code()))
                }
            } catch(e: Exception) {
                NetworkResult.Error(e.message, data = null)
            }
        }
    }
}