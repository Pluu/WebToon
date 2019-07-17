package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.domain.moel.DetailResult
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.ShareItem

interface ShareUseCase {
    operator fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem
}
