package com.pluu.webtoon.data.naver

import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.ShareItem
import com.pluu.webtoon.usecase.ShareUseCase

class NaverDetailShare: ShareUseCase {
    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        return ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = NaverDetailApi.detailCreate(detail.webtoonId, detail.episodeId)
        )
    }
}
