package com.pluu.webtoon.di

import android.content.res.Resources
import android.system.Os.bind
import com.pluu.support.daum.DaumDetailApi
import com.pluu.support.daum.DaumEpisodeApi
import com.pluu.support.daum.DaumWeekApi
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.kakao.KakaoDetailApi
import com.pluu.support.kakao.KakaoEpisodeApi
import com.pluu.support.kakao.KakaoWeekApi
import com.pluu.support.ktoon.OllehDetailApi
import com.pluu.support.ktoon.OllehEpisodeApi
import com.pluu.support.ktoon.OllehWeekApi
import com.pluu.support.nate.NateDetailApi
import com.pluu.support.nate.NateEpisodeApi
import com.pluu.support.nate.NateWeekApi
import com.pluu.support.naver.NaverDetailApi
import com.pluu.support.naver.NaverEpisodeApi
import com.pluu.support.naver.NaverWeekApi
import com.pluu.support.onestore.OneStoreDetailApi
import com.pluu.support.onestore.OneStoreEpisodeApi
import com.pluu.support.onestore.OneStorerWeekApi
import org.koin.dsl.module.module

object Properties {
    const val WEEK_KEY = "WEEK_KEY"
    const val EPISODE_KEY = "EPISODE_KEY"
    const val DETAIL_KEY = "DETAIL_KEY"
}

val webToonModule = module {
    single { NetworkModule(get()) }
    single { createOkHttp() }

    module(Properties.WEEK_KEY) {
        factory { (apiType : NAV_ITEM) -> apiType.asWeekApi(get()) }
    }
    module(Properties.EPISODE_KEY) {
        factory { (apiType: NAV_ITEM) -> apiType.asEpisode(get()) }
    }
    module(Properties.DETAIL_KEY) {
        factory { (apiType: NAV_ITEM) -> apiType.asDetailApi(get()) }
    }
}

private fun NAV_ITEM.asWeekApi(networkModule: NetworkModule): AbstractWeekApi {
    return when (this) {
        NAV_ITEM.NAVER -> NaverWeekApi(networkModule)
        NAV_ITEM.DAUM -> DaumWeekApi(networkModule)
        NAV_ITEM.KTOON -> OllehWeekApi(networkModule)
        NAV_ITEM.KAKAOPAGE -> KakaoWeekApi(networkModule)
        NAV_ITEM.NATE -> NateWeekApi(networkModule)
        NAV_ITEM.ONE_STORE -> OneStorerWeekApi(networkModule)
        else -> throw Resources.NotFoundException("Not Found API")
    }
}

private fun NAV_ITEM.asEpisode(networkModule: NetworkModule): AbstractEpisodeApi {
    return when (this) {
        NAV_ITEM.NAVER -> NaverEpisodeApi(networkModule)
        NAV_ITEM.DAUM -> DaumEpisodeApi(networkModule)
        NAV_ITEM.KTOON -> OllehEpisodeApi(networkModule)
        NAV_ITEM.KAKAOPAGE -> KakaoEpisodeApi(networkModule)
        NAV_ITEM.NATE -> NateEpisodeApi(networkModule)
        NAV_ITEM.ONE_STORE -> OneStoreEpisodeApi(networkModule)
        else -> throw Resources.NotFoundException("Not Found API")
    }
}

private fun NAV_ITEM.asDetailApi(networkModule: NetworkModule): AbstractDetailApi {
    return when (this) {
        NAV_ITEM.NAVER -> NaverDetailApi(networkModule)
        NAV_ITEM.DAUM -> DaumDetailApi(networkModule)
        NAV_ITEM.KTOON -> OllehDetailApi(networkModule)
        NAV_ITEM.KAKAOPAGE -> KakaoDetailApi(networkModule)
        NAV_ITEM.NATE -> NateDetailApi(networkModule)
        NAV_ITEM.ONE_STORE -> OneStoreDetailApi(networkModule)
        else -> throw Resources.NotFoundException("Not Found API")
    }
}