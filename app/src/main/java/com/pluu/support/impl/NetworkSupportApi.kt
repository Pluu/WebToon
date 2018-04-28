package com.pluu.support.impl

import android.content.Context
import com.pluu.webtoon.AppController
import com.pluu.webtoon.network.NetworkTask
import com.pluu.webtoon.network.buildRequestApi
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

/**
 * Network Support API Class
 * Created by pluu on 2017-04-19.
 */
abstract class NetworkSupportApi(context: Context) : IRequest {

    @Inject
    lateinit var client: OkHttpClient

    init {
        @Suppress("LeakingThis")
        (context.applicationContext as AppController).networkComponent.inject(this)
    }

    abstract override val method: REQUEST_METHOD

    abstract override val url: String

    override val params: Map<String, String>
        get() = emptyMap()

    override val headers: Map<String, String>
        get() = emptyMap()

    @Throws(Exception::class)
    protected fun requestApi(request: Request? = null): String {
        val rRequest = request ?: buildRequestApi(this)
        return NetworkTask(client).requestApi(rRequest)
    }
}

enum class REQUEST_METHOD {
    POST, GET
}