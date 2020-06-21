@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.data.network

import com.pluu.webtoon.model.Result
import org.json.JSONObject

val networkFailedException: Exception
    get() = IllegalStateException("Network Not Success")

fun <T : Any> NetworkResult.safeApi(convert: (String) -> T): Result<T> = safeAPi(this, convert)

inline fun NetworkResult.mapJson(): Result<JSONObject> = safeAPi(this) { response ->
    JSONObject(response)
}

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
