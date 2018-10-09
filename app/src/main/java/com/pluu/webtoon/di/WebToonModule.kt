package com.pluu.webtoon.di

import android.content.res.Resources
import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.data.daum.DaumDetailApi
import com.pluu.webtoon.data.daum.DaumDetailShare
import com.pluu.webtoon.data.daum.DaumEpisodeApi
import com.pluu.webtoon.data.daum.DaumWeekApi
import com.pluu.webtoon.data.impl.AbstractDetailApi
import com.pluu.webtoon.data.impl.AbstractEpisodeApi
import com.pluu.webtoon.data.impl.AbstractWeekApi
import com.pluu.webtoon.data.kakao.KakaoDetailApi
import com.pluu.webtoon.data.kakao.KakaoDetailShare
import com.pluu.webtoon.data.kakao.KakaoEpisodeApi
import com.pluu.webtoon.data.kakao.KakaoWeekApi
import com.pluu.webtoon.data.ktoon.KToonDetailApi
import com.pluu.webtoon.data.ktoon.KToonDetailShare
import com.pluu.webtoon.data.ktoon.KToonEpisodeApi
import com.pluu.webtoon.data.ktoon.KToonWeekApi
import com.pluu.webtoon.data.nate.NateDetailApi
import com.pluu.webtoon.data.nate.NateDetailShare
import com.pluu.webtoon.data.nate.NateEpisodeApi
import com.pluu.webtoon.data.nate.NateWeekApi
import com.pluu.webtoon.data.naver.NaverDetailApi
import com.pluu.webtoon.data.naver.NaverDetailShare
import com.pluu.webtoon.data.naver.NaverEpisodeApi
import com.pluu.webtoon.data.naver.NaverWeekApi
import com.pluu.webtoon.data.onestore.OneStoreDetailApi
import com.pluu.webtoon.data.onestore.OneStoreDetailShare
import com.pluu.webtoon.data.onestore.OneStoreEpisodeApi
import com.pluu.webtoon.data.onestore.OneStorerWeekApi
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.ui.detail.DetailViewModel
import com.pluu.webtoon.ui.episode.EpisodeViewModel
import com.pluu.webtoon.ui.intro.IntroUseCase
import com.pluu.webtoon.ui.intro.IntroViewModel
import com.pluu.webtoon.ui.weekly.WeekyViewModel
import com.pluu.webtoon.usecase.*
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module

val introModule = module {
    viewModel { IntroViewModel(IntroUseCase(get())) }
}

val webToonModule = module {
    factory { getProperty<NAV_ITEM>(Property.NAV_ITEM_KEY).asWeekApi(get()) }
    viewModel { (weekPos: Int) ->
        val apiType = getProperty<NAV_ITEM>(Property.NAV_ITEM_KEY)
        WeekyViewModel(
            serviceApi = get(parameters = { parametersOf(apiType) }),
            weekPos = weekPos,
            hasFavoriteUseCase = HasFavoriteUseCase(get(), apiType)
        )
    }

    factory { getProperty<NAV_ITEM>(Property.NAV_ITEM_KEY).asEpisode(get()) }
    viewModel { (info: ToonInfo) ->
        val apiType = getProperty<NAV_ITEM>(Property.NAV_ITEM_KEY)
        EpisodeViewModel(
            serviceApi = get(parameters = { parametersOf(apiType) }),
            info = info,
            episodeListUseCase = EpisodeListUseCase(get(), apiType),
            addFavoriteUseCase = AddFavoriteUseCase(get(), apiType),
            delFavoriteUseCase = RemoveFavoriteUseCase(get(), apiType)
        )
    }

    factory { getProperty<NAV_ITEM>(Property.NAV_ITEM_KEY).asDetailApi(get()) }
    viewModel { (episode: EpisodeInfo) ->
        val apiType = getProperty<NAV_ITEM>(Property.NAV_ITEM_KEY)
        DetailViewModel(
            serviceApi = get(parameters = { parametersOf(episode) }),
            episode = episode,
            readUseCase = ReadUseCase(get(), apiType),
            shareUseCase = get(parameters = { parametersOf(apiType) })
        )
    }
    factory { (type: NAV_ITEM) ->
        when (type) {
            NAV_ITEM.NAVER -> NaverDetailShare()
            NAV_ITEM.DAUM -> DaumDetailShare()
            NAV_ITEM.KTOON -> KToonDetailShare()
            NAV_ITEM.KAKAOPAGE -> KakaoDetailShare()
            NAV_ITEM.NATE -> NateDetailShare()
            NAV_ITEM.ONE_STORE -> OneStoreDetailShare()
            else -> throw Resources.NotFoundException("Not Found API")
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// Custom Extension
///////////////////////////////////////////////////////////////////////////

private fun NAV_ITEM.asWeekApi(networkUseCase: NetworkUseCase): AbstractWeekApi {
    return when (this) {
        NAV_ITEM.NAVER -> NaverWeekApi(networkUseCase)
        NAV_ITEM.DAUM -> DaumWeekApi(networkUseCase)
        NAV_ITEM.KTOON -> KToonWeekApi(networkUseCase)
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
        NAV_ITEM.KTOON -> KToonEpisodeApi(networkUseCase)
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
        NAV_ITEM.KTOON -> KToonDetailApi(networkUseCase)
        NAV_ITEM.KAKAOPAGE -> KakaoDetailApi(networkUseCase)
        NAV_ITEM.NATE -> NateDetailApi(networkUseCase)
        NAV_ITEM.ONE_STORE -> OneStoreDetailApi(networkUseCase)
        else -> throw Resources.NotFoundException("Not Found API")
    }
}

object Property {
    const val NAV_ITEM_KEY = "NAV_ITEM_KEY"
}
