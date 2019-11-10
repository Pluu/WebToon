@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.pluu.webtoon.data.network

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resumeWithException

/**
 * Network Request Task
 * - 실제 Request 하는 로직
 * Created by pluu on 2017-05-02.
 */
class NetworkTask(
    private val client: OkHttpClient
) {
    @Throws(Exception::class)
    suspend fun requestApi(request: Request): NetworkResult {
        val call = client.newCall(request)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(NetworkResult.Success(response.body?.string().orEmpty())) {
                        call.cancel()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    if (continuation.isCancelled) return
                    continuation.resumeWithException(e)
                }
            })

            continuation.invokeOnCancellation {
                try {
                    call.cancel()
                } catch (ex: Throwable) {
                    continuation.resumeWithException(ex)
                }
            }
        }
    }
}
