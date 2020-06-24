package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.domain.model.DetailResult
import com.pluu.webtoon.domain.model.EpisodeInfo
import com.pluu.webtoon.domain.model.ShareItem

interface ShareUseCase {
    operator fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem
}
