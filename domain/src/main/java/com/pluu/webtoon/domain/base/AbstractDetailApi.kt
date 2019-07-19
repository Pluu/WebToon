package com.pluu.webtoon.domain.base

import com.pluu.webtoon.domain.usecase.param.DetailRequest
import com.pluu.webtoon.domain.moel.DetailResult

/**
 * Detail Parse API
 * Created by pluu on 2017-04-20.
 */
interface AbstractDetailApi {
    suspend operator fun invoke(param: DetailRequest): DetailResult
}
