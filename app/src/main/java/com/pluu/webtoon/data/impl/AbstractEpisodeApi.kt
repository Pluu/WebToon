package com.pluu.webtoon.data.impl

import com.pluu.webtoon.data.EpisodeRequest
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.EpisodeResult
import com.pluu.webtoon.item.Result

/**
 * EpisodeInfo API
 * Created by pluu on 2017-04-20.
 */
abstract class AbstractEpisodeApi(
    networkUseCase: NetworkUseCase
) : NetworkSupportApi(networkUseCase) {

    abstract fun parseEpisode(param: EpisodeRequest): Result<EpisodeResult>
}
