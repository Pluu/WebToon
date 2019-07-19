package com.pluu.webtoon.domain.base

import com.pluu.core.Result
import com.pluu.webtoon.domain.usecase.param.EpisodeRequest
import com.pluu.webtoon.domain.moel.EpisodeResult

/**
 * EpisodeInfo API
 * Created by pluu on 2017-04-20.
 */
interface AbstractEpisodeApi {
    suspend operator fun invoke(param: EpisodeRequest): Result<EpisodeResult>
}
