package com.pluu.support.impl

import android.content.Context
import android.content.res.Resources

import com.pluu.support.daum.DaumDetailApi
import com.pluu.support.kakao.KakaoDetailApi
import com.pluu.support.nate.NateDetailApi
import com.pluu.support.naver.NaverDetailApi
import com.pluu.support.olleh.OllehDetailApi
import com.pluu.support.tstore.TStoreDetailApi
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.ShareItem

/**
 * Detail Parse API
 * Created by pluu on 2017-04-20.
 */
abstract class AbstractDetailApi(context: Context) : NetworkSupportApi(context) {

    abstract fun parseDetail(episode: Episode): Detail

    abstract fun getDetailShare(episode: Episode, detail: Detail): ShareItem

    companion object {

        fun getApi(context: Context, item: NAV_ITEM): AbstractDetailApi {
            when (item) {
                NAV_ITEM.NAVER -> return NaverDetailApi(context)
                NAV_ITEM.DAUM -> return DaumDetailApi(context)
                NAV_ITEM.OLLEH -> return OllehDetailApi(context)
                NAV_ITEM.KAKAOPAGE -> return KakaoDetailApi(context)
                NAV_ITEM.NATE -> return NateDetailApi(context)
                NAV_ITEM.T_STORE -> return TStoreDetailApi(context)
                else -> throw Resources.NotFoundException("Not Found API")
            }
        }
    }

}
