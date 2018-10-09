package com.pluu.webtoon.network

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Network Request Task
 * <br></br> 실제 Request 하는 로직
 * Created by pluu on 2017-05-02.
 */
class NetworkTask(private val client: OkHttpClient) {

    @Throws(Exception::class)
    fun requestApi(request: Request): NetworkResult {
        val response = client.newCall(request).execute()
        return if (response.isSuccessful) {
            NetworkResult.Success(response.body()?.string().orEmpty())
        } else {
            NetworkResult.Fail()
        }
    }
}
