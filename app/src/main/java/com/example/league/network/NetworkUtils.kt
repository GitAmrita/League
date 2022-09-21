package com.example.league.network

// Function to return user understandable error messages based on http error codes
fun getErrorMessages(errorCode: Int): String {
    return when(errorCode) {
        in 400..499 -> " bad request, client error."
        in 300..399 -> " temporarily unavailable, try again later"
        in 500..599 -> " internal server error."
        else -> "unknown error occurred"
    }
}