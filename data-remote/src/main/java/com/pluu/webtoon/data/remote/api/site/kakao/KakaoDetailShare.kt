package com.pluu.webtoon.data.remote.api.site.kakao

import com.pluu.webtoon.data.remote.api.ShareApi
import com.pluu.webtoon.model.ShareItem

internal class KakaoDetailShare: ShareApi {
    private val shareUrl =
        "http://page.kakao.com/viewer?productId=%s&categoryUid=10&subCategoryUid=0"
    override fun invoke(param: ShareApi.Param): ShareItem {
        return ShareItem(
            title = "${param.episodeId} / ${param.detailTitle}",
            url = shareUrl.format(param.episodeId)
        )
    }
}
