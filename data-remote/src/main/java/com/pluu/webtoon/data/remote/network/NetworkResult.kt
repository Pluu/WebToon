package com.pluu.webtoon.data.remote.network

/**
 * Network Result 정보
 */
sealed class NetworkResult {
    class Success(val response: String) : NetworkResult()
    class Fail(val throwable: Throwable) : NetworkResult()
}
