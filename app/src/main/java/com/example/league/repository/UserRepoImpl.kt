package com.example.league.repository

import com.example.league.model.User
import com.example.league.network.NetworkResult
import com.example.league.network.RetrofitClient.getRetrofit
import com.example.league.network.UserApi
import com.example.league.network.getErrorMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class UserRepoImpl : UserRepo {

    private val api = getRetrofit().create(UserApi::class.java)

    override suspend fun getUsers(): NetworkResult<List<User>> {
         return withContext(Dispatchers.IO) {
            try {
                val response = api.getUsers()
                if (response.isSuccessful) {
                    response.body() ?. let {
                        NetworkResult.Success(data = it)
                    } ?:  NetworkResult.Error("get user body is empty")
                } else {
                    NetworkResult.Error(getErrorMessages(response.code()))
                }
            } catch(e: Exception) {
                NetworkResult.Error(e.message, data = null)
            }
        }
    }
}