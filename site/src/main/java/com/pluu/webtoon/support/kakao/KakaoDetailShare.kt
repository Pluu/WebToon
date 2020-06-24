package com.pluu.webtoon.support.kakao

import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ShareItem
import com.pluu.webtoon.domain.usecase.ShareUseCase

internal class KakaoDetailShare: ShareUseCase {
    private val shareUrl =
        "http://page.kakao.com/viewer?productId=%s&categoryUid=10&subCategoryUid=0"
    override fun invoke(episode: EpisodeInfo, detail: DetailResult.Detail): ShareItem {
        return ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = shareUrl.format(episode.id)
        )
    }
}
