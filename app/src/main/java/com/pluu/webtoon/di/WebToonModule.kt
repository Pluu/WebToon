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
import com.pluu.webtoon.ui.weekly.WeekyViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module

val webToonModule = module {
    factory { (apiType: NAV_ITEM) -> apiType.asWeekApi(get()) }

    viewModel { (apiType: NAV_ITEM, weekPos: Int) ->
        WeekyViewModel(
            serviceApi = get(parameters = { parametersOf(apiType) }),
            weekPos = weekPos,
            realmHelper = get()
        )
    }
    factory { (apiType: NAV_ITEM) -> apiType.asEpisode(get()) }

    factory { (apiType: NAV_ITEM) -> apiType.asDetailApi(get()) }
}


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
