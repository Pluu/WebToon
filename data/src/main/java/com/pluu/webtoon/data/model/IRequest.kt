package com.pluu.webtoon.data.model

import android.net.Uri
import com.pluu.webtoon.utils.toFormBody
import okhttp3.Request

/**
 * Request Interface
 * Created by pluu on 2017-04-19.
 */
data class IRequest(
    /**
     * http 통신방법.
     * @return httpMethod. GET, POST, PUT, DELETE 등등.
     */
    val method: REQUEST_METHOD = REQUEST_METHOD.GET,

    /**
     * 요청할 target url.
     * @return 요청할 target url.
     */
    val url: String,

    /**
     * http 요청에 필요한 params.
     * @return http 요청에 필요한 params.
     */
    val params: Map<String, String> = emptyMap(),

    /**
     * http 요청에 필요한 headers.
     * @return http 요청에 필요한 headers.
     */
    val headers: Map<String, String> = emptyMap()
)

@Throws(Exception::class)
fun IRequest.buildRequestApi(): Request {
    val builder = Request.Builder().apply {
        for ((key, value) in this@buildRequestApi.headers) {
            addHeader(key, value)
        }

        when (this@buildRequestApi.method) {
            REQUEST_METHOD.POST -> {
                post(this@buildRequestApi.params.toFormBody())
                url(this@buildRequestApi.url)
            }
            REQUEST_METHOD.GET -> {
                val url = Uri.Builder().encodedPath(this@buildRequestApi.url).apply {
                    for ((key, value) in this@buildRequestApi.params) {
                        appendQueryParameter(key, value)
                    }
                }.build()
                url(url.toString())
            }
        }
    }
    return builder.build()
}
