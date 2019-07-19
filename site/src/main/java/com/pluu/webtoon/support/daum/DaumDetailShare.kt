package com.pluu.webtoon.support.daum

import com.pluu.webtoon.domain.moel.DetailResult
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.ShareItem
import com.pluu.webtoon.domain.usecase.ShareUseCase

class DaumDetailShare: ShareUseCase {
    private val shareUrl = "http://m.webtoon.daum.net/m/webtoon/viewer/"

    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        return ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = "$shareUrl$detail.episodeId"
        )
    }
}
