package com.pluu.webtoon.data.remote.api.site.ktoon

import com.pluu.webtoon.data.remote.api.ShareApi
import com.pluu.webtoon.model.ShareItem

internal class KToonDetailShare : ShareApi {
    override fun invoke(param: ShareApi.Param): ShareItem {
        return ShareItem(
            title = "${param.episodeTitle} / ${param.detailTitle}",
            url = "https://v2.myktoon.com/mw/works/viewer.kt?timesseq=${param.episodeId}"
        )
    }
}
