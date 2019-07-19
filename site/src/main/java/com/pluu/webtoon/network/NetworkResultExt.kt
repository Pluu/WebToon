@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.network

import com.pluu.webtoon.data.model.Result
import com.pluu.webtoon.data.network.NetworkResult
import com.pluu.webtoon.data.network.safeAPi
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

inline fun NetworkResult.mapDocument(): Result<Document> =
    safeAPi(this) { response ->
        Jsoup.parse(response)
    }
