package com.pluu.webtoon.di

import com.pluu.webtoon.network.NetworkTask
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class NetworkModule(
    private val okHttpClient: OkHttpClient
) {
    fun createTask(): NetworkTask = NetworkTask(okHttpClient)
}

fun createOkHttp(): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    })
    .build()
