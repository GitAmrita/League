package com.example.league.repository

import com.example.league.model.User
import com.example.league.network.NetworkResult

interface UserRepo {

    suspend fun getUsers(): NetworkResult<List<User>>
}