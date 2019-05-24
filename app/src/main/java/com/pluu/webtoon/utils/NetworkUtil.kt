@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.utils

import com.pluu.webtoon.item.Result
import com.pluu.webtoon.network.NetworkResult
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

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

fun <T : Any> NetworkResult.safeApi(convert: (String) -> T): Result<T> = safeAPi(this, convert)

inline fun NetworkResult.mapDocument(): Result<Document> = safeAPi(this) { response ->
    Jsoup.parse(response)
}

inline fun NetworkResult.mapJson(): Result<JSONObject> = safeAPi(this) { response ->
    JSONObject(response)
}
