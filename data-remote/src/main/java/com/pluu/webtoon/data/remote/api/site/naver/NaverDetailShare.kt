package com.pluu.webtoon.data.remote.api.site.naver

import com.pluu.webtoon.data.remote.api.ShareApi
import com.pluu.webtoon.model.ShareItem

internal class NaverDetailShare: ShareApi {
    override fun invoke(param: ShareApi.Param): ShareItem {
        return ShareItem(
            title = "${param.episodeTitle} / ${param.detailTitle}",
            url = NaverDetailApi.detailCreate(
                param.toonId,
                param.episodeId
            )
        )
    }
}
