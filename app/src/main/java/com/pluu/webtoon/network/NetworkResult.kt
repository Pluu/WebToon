package com.pluu.webtoon.network

/**
 * Network Result 정보
 */
sealed class NetworkResult {
    class Success(val response: String) : NetworkResult()
    object Fail : NetworkResult()
}
