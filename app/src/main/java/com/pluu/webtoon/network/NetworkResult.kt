package com.pluu.webtoon.network

/**
 * Network Result 정보
 */
sealed class NetworkResult(
    // Network 성공 여부
    val isSuccessful: Boolean
) {
    class Success(val response: String) : NetworkResult(true)
    class Fail: NetworkResult(false)
}
