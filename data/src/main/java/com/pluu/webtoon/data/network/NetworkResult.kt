package com.pluu.webtoon.data.network

/**
 * Network Result 정보
 */
sealed class NetworkResult {
    class Success(val response: String) : NetworkResult()
    object Fail : NetworkResult()
}
