package com.pluu.webtoon.support.ktoon

import com.pluu.webtoon.domain.moel.DetailResult
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.ShareItem
import com.pluu.webtoon.domain.usecase.ShareUseCase

internal class KToonDetailShare : ShareUseCase {
    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        return ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = "https://v2.myktoon.com/mw/works/viewer.kt?timesseq=${detail.episodeId}"
        )
    }
}
