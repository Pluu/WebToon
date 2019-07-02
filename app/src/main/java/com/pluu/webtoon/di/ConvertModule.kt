package com.pluu.webtoon.di

import android.content.res.Resources
import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.data.daum.DaumDetailApi
import com.pluu.webtoon.data.daum.DaumDetailShare
import com.pluu.webtoon.data.daum.DaumEpisodeApi
import com.pluu.webtoon.data.daum.DaumWeekApi
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
import com.pluu.webtoon.usecase.DetailUseCase
import com.pluu.webtoon.usecase.EpisodeUseCase
import com.pluu.webtoon.usecase.WeeklyUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

@Suppress("IMPLICIT_CAST_TO_ANY")
val convertModule = module {
    factory<WeeklyUseCase>(named(UseCaseProperties.WEEKLY_USECASE)) {
        when (getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)) {
            NAV_ITEM.NAVER -> NaverWeekApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.DAUM -> DaumWeekApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.KTOON -> KToonWeekApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.KAKAOPAGE -> KakaoWeekApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.NATE -> NateWeekApi(get(named(AppProperties.NETWORK)))
            else -> throw Resources.NotFoundException("Not Found API")
        }
    }

    factory<EpisodeUseCase>(named(UseCaseProperties.EPISODE_USECASE)) {
        when (getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)) {
            NAV_ITEM.NAVER -> NaverEpisodeApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.DAUM -> DaumEpisodeApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.KTOON -> KToonEpisodeApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.KAKAOPAGE -> KakaoEpisodeApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.NATE -> NateEpisodeApi(get(named(AppProperties.NETWORK)))
            else -> throw Resources.NotFoundException("Not Found API")
        }
    }

    factory<DetailUseCase>(named(UseCaseProperties.DETAIL_USECASE)) {
        when (getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)) {
            NAV_ITEM.NAVER -> NaverDetailApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.DAUM -> DaumDetailApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.KTOON -> KToonDetailApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.KAKAOPAGE -> KakaoDetailApi(get(named(AppProperties.NETWORK)))
            NAV_ITEM.NATE -> NateDetailApi(get(named(AppProperties.NETWORK)))
            else -> throw Resources.NotFoundException("Not Found API")
        }
    }

    factory {
        when (getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)) {
            NAV_ITEM.NAVER -> NaverDetailShare()
            NAV_ITEM.DAUM -> DaumDetailShare()
            NAV_ITEM.KTOON -> KToonDetailShare()
            NAV_ITEM.KAKAOPAGE -> KakaoDetailShare()
            NAV_ITEM.NATE -> NateDetailShare()
            else -> throw Resources.NotFoundException("Not Found API")
        }
    }
}

object UseCaseProperties {
    const val WEEKLY_USECASE = "WEEKLY_USECASE"
    const val EPISODE_USECASE = "EPISODE_USECASE"
    const val DETAIL_USECASE = "DETAIL_USECASE"
}
