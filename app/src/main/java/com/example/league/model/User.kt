package com.example.league.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val userName: String,
    @SerializedName("avatar")
    val avatarUrl: String
)
