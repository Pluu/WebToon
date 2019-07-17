package com.pluu.webtoon.support.kakao

import com.pluu.webtoon.domain.moel.DetailResult
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.ShareItem
import com.pluu.webtoon.domain.usecase.ShareUseCase

class KakaoDetailShare: ShareUseCase {
    private val shareUrl =
        "http://page.kakao.com/viewer?productId=%s&categoryUid=10&subCategoryUid=0"
    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        return ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = shareUrl.format(episode.id)
        )
    }
}
