package com.pluu.webtoon.di

import com.pluu.webtoon.network.NetworkResult
import com.pluu.webtoon.network.NetworkTask
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkUseCase(
    private val okHttpClient: OkHttpClient
) {
    fun requestApi(request: Request): NetworkResult {
        return createTask().requestApi(request)
    }

    private fun createTask(): NetworkTask = NetworkTask(okHttpClient)
}
