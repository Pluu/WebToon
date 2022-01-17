@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.data.remote.utils

import com.pluu.webtoon.data.remote.network.NetworkResult
import com.pluu.webtoon.model.Result
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

inline fun NetworkResult.mapDocument(): Result<Document> = safeAPi(this) { response ->
    Jsoup.parse(response)
}
