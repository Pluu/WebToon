package com.pluu.webtoon.support.naver

import com.pluu.webtoon.domain.model.DetailResult
import com.pluu.webtoon.domain.model.EpisodeInfo
import com.pluu.webtoon.domain.model.ShareItem
import com.pluu.webtoon.domain.usecase.ShareUseCase

internal class NaverDetailShare: ShareUseCase {
    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        return ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = NaverDetailApi.detailCreate(
                detail.webtoonId,
                detail.episodeId
            )
        )
    }
}
