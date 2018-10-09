package com.pluu.webtoon.utils

import com.pluu.webtoon.item.Result
import com.pluu.webtoon.network.NetworkResult

val networkFailedException: Exception
    get() = IllegalStateException("Network Not Success")


fun <T : Any> safeAPi(result: NetworkResult, convert: (String) -> T): Result<T> {
    return when (result) {
        is NetworkResult.Success -> {
            try {
                Result.Success(convert(result.response))
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(e)
            }
        }
        else -> Result.Error(networkFailedException)
    }
}
