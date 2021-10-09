package com.pluu.webtoon.support.daum

import com.pluu.webtoon.domain.usecase.ShareUseCase
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ShareItem

internal class DaumDetailShare : ShareUseCase {
    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        throw IllegalStateException("Un-support kakao webtoon")
    }
}
