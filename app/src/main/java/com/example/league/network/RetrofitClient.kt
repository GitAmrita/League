package com.example.league.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://engineering.league.dev/challenge/api/"
    private const val TIME_OUT = 60L
    private const val API_KEY = "9E633129DCCD9A621C771BB0A88DAC7C"

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                    .addHeader("x-access-token", API_KEY)
                    .build()
            chain.proceed(newRequest)
        }
        .build()

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
}