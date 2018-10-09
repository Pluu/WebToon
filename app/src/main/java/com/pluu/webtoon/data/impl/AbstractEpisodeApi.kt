package com.pluu.webtoon.data.impl

import com.pluu.webtoon.data.EpisodeRequest
import com.pluu.webtoon.item.EpisodeResult
import com.pluu.webtoon.item.Result

/**
 * EpisodeInfo API
 * Created by pluu on 2017-04-20.
 */
interface AbstractEpisodeApi {
    operator fun invoke(param: EpisodeRequest): Result<EpisodeResult>
}
