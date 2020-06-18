package com.pluu.webtoon.di

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.common.Session
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.domain.usecase.DetailUseCase
import com.pluu.webtoon.domain.usecase.EpisodeUseCase
import com.pluu.webtoon.domain.usecase.ShareUseCase
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.support.daum.DaumDetailApi
import com.pluu.webtoon.support.daum.DaumDetailShare
import com.pluu.webtoon.support.daum.DaumEpisodeApi
import com.pluu.webtoon.support.daum.DaumWeekApi
import com.pluu.webtoon.support.kakao.KakaoDetailApi
import com.pluu.webtoon.support.kakao.KakaoDetailShare
import com.pluu.webtoon.support.kakao.KakaoEpisodeApi
import com.pluu.webtoon.support.kakao.KakaoWeekApi
import com.pluu.webtoon.support.ktoon.KToonDetailApi
import com.pluu.webtoon.support.ktoon.KToonDetailShare
import com.pluu.webtoon.support.ktoon.KToonEpisodeApi
import com.pluu.webtoon.support.ktoon.KToonWeekApi
import com.pluu.webtoon.support.nate.NateDetailApi
import com.pluu.webtoon.support.nate.NateDetailShare
import com.pluu.webtoon.support.nate.NateEpisodeApi
import com.pluu.webtoon.support.nate.NateWeekApi
import com.pluu.webtoon.support.naver.NaverDetailApi
import com.pluu.webtoon.support.naver.NaverDetailShare
import com.pluu.webtoon.support.naver.NaverEpisodeApi
import com.pluu.webtoon.support.naver.NaverWeekApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Named
import javax.inject.Qualifier

@Module
@InstallIn(ApplicationComponent::class)
object SiteModule {

    @Provides
    fun provideWeeklyUseCase(
        session: Session,
        networkUseCase: INetworkUseCase
    ): WeeklyUseCase = when (session.navi) {
        NAV_ITEM.NAVER -> NaverWeekApi(networkUseCase)
        NAV_ITEM.DAUM -> DaumWeekApi(networkUseCase)
        NAV_ITEM.KTOON -> KToonWeekApi(networkUseCase)
        NAV_ITEM.KAKAOPAGE -> KakaoWeekApi(networkUseCase)
        NAV_ITEM.NATE -> NateWeekApi(networkUseCase)
    }

    @Provides
    fun provideEpisodeUseCase(
        session: Session,
        networkUseCase: INetworkUseCase
    ): EpisodeUseCase = when (session.navi) {
        NAV_ITEM.NAVER -> NaverEpisodeApi(networkUseCase)
        NAV_ITEM.DAUM -> DaumEpisodeApi(networkUseCase)
        NAV_ITEM.KTOON -> KToonEpisodeApi(networkUseCase)
        NAV_ITEM.KAKAOPAGE -> KakaoEpisodeApi(networkUseCase)
        NAV_ITEM.NATE -> NateEpisodeApi(networkUseCase)
    }

    @Provides
    fun provideDetailUseCase(
        session: Session,
        networkUseCase: INetworkUseCase
    ): DetailUseCase = when (session.navi) {
        NAV_ITEM.NAVER -> NaverDetailApi(networkUseCase)
        NAV_ITEM.DAUM -> DaumDetailApi(networkUseCase)
        NAV_ITEM.KTOON -> KToonDetailApi(networkUseCase)
        NAV_ITEM.KAKAOPAGE -> KakaoDetailApi(networkUseCase)
        NAV_ITEM.NATE -> NateDetailApi(networkUseCase)
    }

    @Provides
    fun provideShareUseCase(
        session: Session
    ): ShareUseCase = when (session.navi) {
        NAV_ITEM.NAVER -> NaverDetailShare()
        NAV_ITEM.DAUM -> DaumDetailShare()
        NAV_ITEM.KTOON -> KToonDetailShare()
        NAV_ITEM.KAKAOPAGE -> KakaoDetailShare()
        NAV_ITEM.NATE -> NateDetailShare()
    }
}
