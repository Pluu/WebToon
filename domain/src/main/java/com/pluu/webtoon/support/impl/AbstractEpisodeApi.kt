package com.pluu.webtoon.support.impl

import com.pluu.webtoon.data.network.EpisodeRequest
import com.pluu.core.Result
import com.pluu.webtoon.domain.moel.EpisodeResult

/**
 * EpisodeInfo API
 * Created by pluu on 2017-04-20.
 */
interface AbstractEpisodeApi {
    suspend operator fun invoke(param: EpisodeRequest): Result<EpisodeResult>
}
