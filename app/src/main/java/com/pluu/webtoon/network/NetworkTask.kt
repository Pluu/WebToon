package com.pluu.webtoon.network

import android.net.Uri
import com.pluu.support.impl.IRequest
import com.pluu.support.impl.REQUEST_METHOD
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Network Request Task
 * <br></br> 실제 Request 하는 로직
 * Created by pluu on 2017-05-02.
 */
class NetworkTask(private val client: OkHttpClient) {

    @Throws(Exception::class)
    fun requestApi(request: IRequest): String {
        val builder = Request.Builder().apply {
            for ((key, value) in request.headers) {
                addHeader(key, value)
            }

            when (request.method) {
                REQUEST_METHOD.POST -> {
                    val requestBody = FormBody.Builder().apply {
                        for ((key, value) in request.params) {
                            add(key, value)
                        }
                    }.build()
                    post(requestBody)
                    url(request.url)
                }
                REQUEST_METHOD.GET -> {
                    val url = Uri.Builder().encodedPath(request.url).apply {
                        for ((key, value) in request.params) {
                            appendQueryParameter(key, value)
                        }
                    }.build()
                    url(url.toString())
                }
            }
        }
        return requestApi(builder.build())
    }

    @Throws(Exception::class)
    fun requestApi(request: Request): String =
        client.newCall(request).execute().body()?.string() ?: ""

}
