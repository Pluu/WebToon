package com.pluu.webtoon.data.daum

import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.ShareItem
import com.pluu.webtoon.usecase.ShareUseCase

class DaumDetailShare: ShareUseCase {
    private val shareUrl = "http://m.webtoon.daum.net/m/webtoon/viewer/"

    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        return ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = "$shareUrl$detail.episodeId"
        )
    }
}
