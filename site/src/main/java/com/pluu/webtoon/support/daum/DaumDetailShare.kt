package com.pluu.webtoon.support.daum

import com.pluu.webtoon.domain.usecase.ShareUseCase
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ShareItem

internal class DaumDetailShare: ShareUseCase {
    private val shareUrl = "http://m.webtoon.daum.net/m/webtoon/viewer/"

    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        return ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = "$shareUrl$detail.episodeId"
        )
    }
}
