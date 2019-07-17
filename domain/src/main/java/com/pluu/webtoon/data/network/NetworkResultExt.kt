@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.data.network

import com.pluu.core.Result
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

inline fun NetworkResult.mapDocument(): Result<Document> = safeAPi(this) { response ->
    Jsoup.parse(response)
}
