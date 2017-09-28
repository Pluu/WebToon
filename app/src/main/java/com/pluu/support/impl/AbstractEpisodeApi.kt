package com.pluu.support.impl

import android.content.Context
import android.content.res.Resources
import com.pluu.support.daum.DaumEpisodeApi
import com.pluu.support.kakao.KakaoEpisodeApi
import com.pluu.support.nate.NateEpisodeApi
import com.pluu.support.naver.NaverEpisodeApi
import com.pluu.support.olleh.OllehEpisodeApi
import com.pluu.support.tstore.TStoreEpisodeApi
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.WebToonInfo

/**
 * Episode API
 * Created by pluu on 2017-04-20.
 */
abstract class AbstractEpisodeApi(context: Context) : NetworkSupportApi(context) {

    open fun init() {}

    abstract fun parseEpisode(info: WebToonInfo): EpisodePage

    abstract fun moreParseEpisode(item: EpisodePage): String?

    abstract fun getFirstEpisode(item: Episode): Episode?

    companion object {

        fun getApi(context: Context, item: NAV_ITEM): AbstractEpisodeApi = when (item) {
            NAV_ITEM.NAVER -> NaverEpisodeApi(context)
            NAV_ITEM.DAUM -> DaumEpisodeApi(context)
            NAV_ITEM.OLLEH -> OllehEpisodeApi(context)
            NAV_ITEM.KAKAOPAGE -> KakaoEpisodeApi(context)
            NAV_ITEM.NATE -> NateEpisodeApi(context)
            NAV_ITEM.T_STORE -> TStoreEpisodeApi(context)
            else -> throw Resources.NotFoundException("Not Found API")
        }
    }

}
