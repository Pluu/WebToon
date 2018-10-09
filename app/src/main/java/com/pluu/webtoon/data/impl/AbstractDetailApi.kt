package com.pluu.webtoon.data.impl

import com.pluu.webtoon.data.DetailRequest
import com.pluu.webtoon.item.DetailResult

/**
 * Detail Parse API
 * Created by pluu on 2017-04-20.
 */
interface AbstractDetailApi {
    operator fun invoke(param: DetailRequest): DetailResult
}
