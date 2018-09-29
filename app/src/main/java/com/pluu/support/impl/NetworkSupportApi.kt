package com.pluu.support.impl

import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.network.buildRequestApi
import okhttp3.Request

/**
 * Network Support API Class
 * Created by pluu on 2017-04-19.
 */
abstract class NetworkSupportApi(
    private val networkUseCase: NetworkUseCase
) : IRequest {
    abstract override val method: REQUEST_METHOD

    abstract override val url: String

    override val params: Map<String, String>
        get() = emptyMap()

    override val headers: Map<String, String>
        get() = emptyMap()

    @Throws(Exception::class)
    protected fun requestApi(request: Request? = null): String {
        val rRequest = request ?: buildRequestApi(this)
        return networkUseCase.createTask().requestApi(rRequest)
    }
}

enum class REQUEST_METHOD {
    POST, GET
}
