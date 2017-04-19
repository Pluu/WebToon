package com.pluu.support.impl

import android.content.Context
import android.content.res.Resources

import com.pluu.support.daum.DaumEpisodeApi
import com.pluu.support.impl.ServiceConst.NAV_ITEM
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

    abstract fun moreParseEpisode(item: EpisodePage): String

    abstract fun getFirstEpisode(item: Episode): Episode

    companion object {

        @JvmStatic
        fun getApi(context: Context, item: NAV_ITEM): AbstractEpisodeApi {
            when (item) {
                ServiceConst.NAV_ITEM.NAVER -> return NaverEpisodeApi(context)
                ServiceConst.NAV_ITEM.DAUM -> return DaumEpisodeApi(context)
                ServiceConst.NAV_ITEM.OLLEH -> return OllehEpisodeApi(context)
                ServiceConst.NAV_ITEM.KAKAOPAGE -> return KakaoEpisodeApi(context)
                ServiceConst.NAV_ITEM.NATE -> return NateEpisodeApi(context)
                ServiceConst.NAV_ITEM.T_STORE -> return TStoreEpisodeApi(context)
                else -> throw Resources.NotFoundException("Not Found API")
            }
        }
    }

}
