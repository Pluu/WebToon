package com.pluu.webtoon.support.impl

import com.pluu.webtoon.data.network.DetailRequest
import com.pluu.webtoon.domain.moel.DetailResult

/**
 * Detail Parse API
 * Created by pluu on 2017-04-20.
 */
interface AbstractDetailApi {
    suspend operator fun invoke(param: DetailRequest): DetailResult
}
