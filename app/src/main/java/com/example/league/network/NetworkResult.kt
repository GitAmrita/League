package com.example.league.network

// This class captures the response from the api.
// Used a sealed class in order to restrict hierarchy.
// We have a data class success object which contains the success data and a data class
// error object which contains the error message.
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T): NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null): NetworkResult<T>(data, message)
}