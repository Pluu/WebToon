package com.pluu.webtoon.data.network

import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.model.buildRequestApi
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkUseCase(
    private val okHttpClient: OkHttpClient
) : INetworkUseCase {

    @Throws(Exception::class)
    override suspend fun requestApi(request: IRequest): NetworkResult {
        return requestApi(request.buildRequestApi())
    }

    @Throws(Exception::class)
    override suspend fun requestApi(request: Request): NetworkResult {
        return createTask().requestApi(request)
    }

    private fun createTask(): NetworkTask =
        NetworkTask(okHttpClient)
}

interface INetworkUseCase {
    suspend fun requestApi(request: IRequest): NetworkResult
    suspend fun requestApi(request: Request): NetworkResult
}
