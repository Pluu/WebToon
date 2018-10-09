package com.pluu.webtoon.usecase

import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.ShareItem

interface ShareUseCase {
    operator fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem
}
