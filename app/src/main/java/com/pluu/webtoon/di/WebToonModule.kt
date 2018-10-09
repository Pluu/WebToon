package com.pluu.webtoon.di

import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.ui.detail.DetailViewModel
import com.pluu.webtoon.ui.episode.EpisodeViewModel
import com.pluu.webtoon.ui.intro.IntroUseCase
import com.pluu.webtoon.ui.intro.IntroViewModel
import com.pluu.webtoon.ui.weekly.WeekyViewModel
import com.pluu.webtoon.usecase.*
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val introModule = module {
    viewModel { IntroViewModel(IntroUseCase(get())) }
}

val webToonModule = module {
    viewModel { (weekPos: Int) ->
        val apiType = getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)
        WeekyViewModel(
            weekPos = weekPos,
            weeklyUseCase = get(UseCaseProperties.WEEKLY_USECASE),
            hasFavoriteUseCase = HasFavoriteUseCase(get(), apiType)
        )
    }

    viewModel { (info: ToonInfo) ->
        val apiType = getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)
        EpisodeViewModel(
            info = info,
            episodeUseCase = get(UseCaseProperties.EPISODE_USECASE),
            readEpisodeListUseCase = ReadEpisodeListUseCase(get(), apiType),
            addFavoriteUseCase = AddFavoriteUseCase(get(), apiType),
            delFavoriteUseCase = RemoveFavoriteUseCase(get(), apiType)
        )
    }

    viewModel { (episode: EpisodeInfo) ->
        val apiType = getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)
        DetailViewModel(
            episode = episode,
            detailUseCase = get(UseCaseProperties.DETAIL_USECASE),
            readUseCase = ReadUseCase(get(), apiType),
            shareUseCase = get()
        )
    }
}

object ServiceProperties {
    const val NAV_ITEM = "NAV_ITEM"
}
