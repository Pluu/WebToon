package com.pluu.webtoon.data.kakao

import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.ShareItem
import com.pluu.webtoon.usecase.ShareUseCase

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
