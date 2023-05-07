package com.pluu.webtoon.data.remote.di

import com.pluu.webtoon.data.remote.api.DetailApi
import com.pluu.webtoon.data.remote.api.EpisodeApi
import com.pluu.webtoon.data.remote.api.ShareApi
import com.pluu.webtoon.data.remote.api.WeeklyApi
import com.pluu.webtoon.data.remote.api.site.daum.DaumDetailApi
import com.pluu.webtoon.data.remote.api.site.daum.DaumDetailShare
import com.pluu.webtoon.data.remote.api.site.daum.DaumEpisodeApi
import com.pluu.webtoon.data.remote.api.site.daum.DaumWeekApi
import com.pluu.webtoon.data.remote.api.site.kakao.KakaoDetailApi
import com.pluu.webtoon.data.remote.api.site.kakao.KakaoDetailShare
import com.pluu.webtoon.data.remote.api.site.kakao.KakaoEpisodeApi
import com.pluu.webtoon.data.remote.api.site.kakao.KakaoWeekApi
import com.pluu.webtoon.data.remote.api.site.ktoon.KToonDetailApi
import com.pluu.webtoon.data.remote.api.site.ktoon.KToonDetailShare
import com.pluu.webtoon.data.remote.api.site.ktoon.KToonEpisodeApi
import com.pluu.webtoon.data.remote.api.site.ktoon.KToonWeekApi
import com.pluu.webtoon.data.remote.api.site.naver.NaverDetailApi
import com.pluu.webtoon.data.remote.api.site.naver.NaverDetailShare
import com.pluu.webtoon.data.remote.api.site.naver.NaverEpisodeApi
import com.pluu.webtoon.data.remote.api.site.naver.NaverWeekApi
import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.data.remote.utils.ResourceLoader
import com.pluu.webtoon.model.NAV_ITEM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    @Provides
    fun provideWeeklyApi(
        naviItem: NAV_ITEM,
        networkUseCase: INetworkUseCase,
        resourceLoader: ResourceLoader
    ): WeeklyApi = when (naviItem) {
        NAV_ITEM.NAVER -> NaverWeekApi(networkUseCase)
        NAV_ITEM.DAUM -> DaumWeekApi(networkUseCase)
        NAV_ITEM.KTOON -> KToonWeekApi(networkUseCase)
        NAV_ITEM.KAKAOPAGE -> KakaoWeekApi(networkUseCase, resourceLoader)
    }

    @Provides
    fun provideEpisodeApi(
        naviItem: NAV_ITEM,
        networkUseCase: INetworkUseCase
    ): EpisodeApi = when (naviItem) {
        NAV_ITEM.NAVER -> NaverEpisodeApi(networkUseCase)
        NAV_ITEM.DAUM -> DaumEpisodeApi(networkUseCase)
        NAV_ITEM.KTOON -> KToonEpisodeApi(networkUseCase)
        NAV_ITEM.KAKAOPAGE -> KakaoEpisodeApi(networkUseCase)
    }

    @Provides
    fun provideDetailApi(
        naviItem: NAV_ITEM,
        networkUseCase: INetworkUseCase
    ): DetailApi = when (naviItem) {
        NAV_ITEM.NAVER -> NaverDetailApi(networkUseCase)
        NAV_ITEM.DAUM -> DaumDetailApi(networkUseCase)
        NAV_ITEM.KTOON -> KToonDetailApi(networkUseCase)
        NAV_ITEM.KAKAOPAGE -> KakaoDetailApi(networkUseCase)
    }

    @Provides
    fun provideShareApi(
        naviItem: NAV_ITEM
    ): ShareApi = when (naviItem) {
        NAV_ITEM.NAVER -> NaverDetailShare()
        NAV_ITEM.DAUM -> DaumDetailShare()
        NAV_ITEM.KTOON -> KToonDetailShare()
        NAV_ITEM.KAKAOPAGE -> KakaoDetailShare()
    }
}
