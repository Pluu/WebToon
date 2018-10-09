package com.pluu.webtoon.data.impl

import com.pluu.webtoon.data.DetailRequest
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.DetailResult

/**
 * Detail Parse API
 * Created by pluu on 2017-04-20.
 */
abstract class AbstractDetailApi(
    networkUseCase: NetworkUseCase
) : NetworkSupportApi(networkUseCase) {

    abstract fun parseDetail(param: DetailRequest): DetailResult
}
