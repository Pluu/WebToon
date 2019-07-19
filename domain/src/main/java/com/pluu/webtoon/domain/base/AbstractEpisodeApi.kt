package com.pluu.webtoon.domain.base

import com.pluu.webtoon.data.model.Result
import com.pluu.webtoon.domain.moel.EpisodeResult
import com.pluu.webtoon.domain.usecase.param.EpisodeRequest

/**
 * EpisodeInfo API
 * Created by pluu on 2017-04-20.
 */
interface AbstractEpisodeApi {
    suspend operator fun invoke(param: EpisodeRequest): Result<EpisodeResult>
}
