package com.pluu.webtoon.data.ktoon

import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.ShareItem
import com.pluu.webtoon.usecase.ShareUseCase

class KToonDetailShare: ShareUseCase {
    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        return ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = "https://v2.myktoon.com/mw/works/viewer.kt?timesseq=${detail.episodeId}"
        )
    }
}
