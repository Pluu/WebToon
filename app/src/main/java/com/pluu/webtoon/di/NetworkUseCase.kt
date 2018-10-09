package com.pluu.webtoon.di

import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.buildRequestApi
import com.pluu.webtoon.network.NetworkResult
import com.pluu.webtoon.network.NetworkTask
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkUseCase(
    private val okHttpClient: OkHttpClient
) : INetworkUseCase {

    @Throws(Exception::class)
    override fun requestApi(request: IRequest): NetworkResult {
        return requestApi(request.buildRequestApi())
    }

    @Throws(Exception::class)
    override fun requestApi(request: Request): NetworkResult {
        return createTask().requestApi(request)
    }

    private fun createTask(): NetworkTask = NetworkTask(okHttpClient)
}

interface INetworkUseCase {
    fun requestApi(request: IRequest): NetworkResult
    fun requestApi(request: Request): NetworkResult
}
