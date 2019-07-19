package com.pluu.webtoon.support.nate

import com.pluu.webtoon.domain.moel.DetailResult
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.ShareItem
import com.pluu.webtoon.domain.usecase.ShareUseCase

class NateDetailShare: ShareUseCase {
    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        return ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = NateDetailApi.detailCreate(
                detail.webtoonId,
                detail.episodeId
            )
        )
    }
}
