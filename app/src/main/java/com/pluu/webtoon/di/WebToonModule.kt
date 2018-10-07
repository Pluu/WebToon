package com.pluu.webtoon.di

import android.content.res.Resources
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
import com.pluu.webtoon.usecase.EpisodeListUseCase
import com.pluu.webtoon.usecase.FavoriteUseCase
import com.pluu.webtoon.usecase.WeeklyUseCase
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.ui.episode.EpisodeViewModel
import com.pluu.webtoon.ui.intro.IntroUseCase
import com.pluu.webtoon.ui.intro.IntroViewModel
import com.pluu.webtoon.ui.weekly.WeekyViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module

val introModule = module {
    viewModel {
        IntroViewModel(IntroUseCase(get()))
    }
}

val webToonModule = module {
    factory { getProperty<NAV_ITEM>(Property.NAV_ITEM_KEY).asWeekApi(get()) }
    viewModel { (weekPos: Int) ->
        val apiType = getProperty<NAV_ITEM>(Property.NAV_ITEM_KEY)
        WeekyViewModel(
            serviceApi = get(parameters = { parametersOf(apiType) }),
            weekPos = weekPos,
            useCase = WeeklyUseCase(get(), apiType)
        )
    }

    factory { getProperty<NAV_ITEM>(Property.NAV_ITEM_KEY).asEpisode(get()) }
    viewModel { (info: WebToonInfo) ->
        val apiType = getProperty<NAV_ITEM>(Property.NAV_ITEM_KEY)
        EpisodeViewModel(
            serviceApi = get(parameters = { parametersOf(apiType) }),
            info = info,
            listUseCase = EpisodeListUseCase(get(), apiType),
            favoriteUseCase = FavoriteUseCase(get(), apiType)
        )
    }

    factory { getProperty<NAV_ITEM>(Property.NAV_ITEM_KEY).asDetailApi(get()) }
}

///////////////////////////////////////////////////////////////////////////
// Custom Extension
///////////////////////////////////////////////////////////////////////////

private fun NAV_ITEM.asWeekApi(networkUseCase: NetworkUseCase): AbstractWeekApi {
    return when (this) {
        NAV_ITEM.NAVER -> NaverWeekApi(networkUseCase)
        NAV_ITEM.DAUM -> DaumWeekApi(networkUseCase)
        NAV_ITEM.KTOON -> OllehWeekApi(networkUseCase)
        NAV_ITEM.KAKAOPAGE -> KakaoWeekApi(networkUseCase)
        NAV_ITEM.NATE -> NateWeekApi(networkUseCase)
        NAV_ITEM.ONE_STORE -> OneStorerWeekApi(networkUseCase)
        else -> throw Resources.NotFoundException("Not Found API")
    }
}

private fun NAV_ITEM.asEpisode(networkUseCase: NetworkUseCase): AbstractEpisodeApi {
    return when (this) {
        NAV_ITEM.NAVER -> NaverEpisodeApi(networkUseCase)
        NAV_ITEM.DAUM -> DaumEpisodeApi(networkUseCase)
        NAV_ITEM.KTOON -> OllehEpisodeApi(networkUseCase)
        NAV_ITEM.KAKAOPAGE -> KakaoEpisodeApi(networkUseCase)
        NAV_ITEM.NATE -> NateEpisodeApi(networkUseCase)
        NAV_ITEM.ONE_STORE -> OneStoreEpisodeApi(networkUseCase)
        else -> throw Resources.NotFoundException("Not Found API")
    }
}

private fun NAV_ITEM.asDetailApi(networkUseCase: NetworkUseCase): AbstractDetailApi {
    return when (this) {
        NAV_ITEM.NAVER -> NaverDetailApi(networkUseCase)
        NAV_ITEM.DAUM -> DaumDetailApi(networkUseCase)
        NAV_ITEM.KTOON -> OllehDetailApi(networkUseCase)
        NAV_ITEM.KAKAOPAGE -> KakaoDetailApi(networkUseCase)
        NAV_ITEM.NATE -> NateDetailApi(networkUseCase)
        NAV_ITEM.ONE_STORE -> OneStoreDetailApi(networkUseCase)
        else -> throw Resources.NotFoundException("Not Found API")
    }
}

object Property {
    const val NAV_ITEM_KEY = "NAV_ITEM_KEY"
}
