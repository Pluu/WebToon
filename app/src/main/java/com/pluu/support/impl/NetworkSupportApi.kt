package com.pluu.support.impl

import android.content.Context
import com.pluu.webtoon.AppController
import com.pluu.webtoon.network.NetworkTask
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
        (context.applicationContext as AppController).networkComponent.inject(this)
    }

    abstract override val method: String

    abstract override val url: String

    override val params: Map<String, String>
        get() = emptyMap()

    override val headers: Map<String, String>
        get() = emptyMap()

    @Throws(Exception::class)
    protected fun requestApi(): String {
        return NetworkTask(client).requestApi(this)
    }

    @Throws(Exception::class)
    protected fun requestApi(request: Request): String {
        return NetworkTask(client).requestApi(request)
    }

    companion object {

        val POST = "POST"
        val GET = "GET"

    }

}
