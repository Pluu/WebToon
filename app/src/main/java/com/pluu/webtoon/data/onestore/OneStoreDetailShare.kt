package com.pluu.webtoon.data.onestore

import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.ShareItem
import com.pluu.webtoon.usecase.ShareUseCase

class OneStoreDetailShare: ShareUseCase {
    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        return ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonDetail.omp?prodId=${detail.episodeId}&PrePageNm=/detail/webtoon/mw"
        )
    }
}
