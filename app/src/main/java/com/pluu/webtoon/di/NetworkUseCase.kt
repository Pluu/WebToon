package com.pluu.webtoon.di

import com.pluu.webtoon.network.NetworkTask
import okhttp3.OkHttpClient

class NetworkUseCase(
    private val okHttpClient: OkHttpClient
) {
    fun createTask(): NetworkTask = NetworkTask(okHttpClient)
}
