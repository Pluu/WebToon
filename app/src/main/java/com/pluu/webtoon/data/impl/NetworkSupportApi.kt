package com.pluu.webtoon.data.impl

import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.buildRequestApi
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.network.NetworkResult
import okhttp3.Request

/**
 * Network Support API Class
 * Created by pluu on 2017-04-19.
 */
abstract class NetworkSupportApi(
    private val networkUseCase: NetworkUseCase
) {
    @Throws(Exception::class)
    protected fun requestApi(request: IRequest): NetworkResult {
        return requestApi(request.buildRequestApi())
    }

    @Throws(Exception::class)
    protected fun requestApi(request: Request): NetworkResult {
        return networkUseCase.requestApi(request)
    }
}
