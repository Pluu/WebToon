package com.pluu.webtoon.data.remote.api.site.daum

import com.pluu.webtoon.data.remote.api.ShareApi
import com.pluu.webtoon.model.ShareItem

internal class DaumDetailShare : ShareApi {
    override fun invoke(param: ShareApi.Param): ShareItem {
        throw IllegalStateException("Un-support kakao webtoon")
    }
}
