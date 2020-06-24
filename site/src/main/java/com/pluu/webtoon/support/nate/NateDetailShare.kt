package com.pluu.webtoon.support.nate

import com.pluu.webtoon.domain.model.DetailResult
import com.pluu.webtoon.domain.model.EpisodeInfo
import com.pluu.webtoon.domain.model.ShareItem
import com.pluu.webtoon.domain.usecase.ShareUseCase

internal class NateDetailShare: ShareUseCase {
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
