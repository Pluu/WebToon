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
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val introModule = module {
    viewModel {
        IntroViewModel(
            useCase = IntroUseCase(get())
        )
    }
}

val webToonModule = module {
    viewModel { (weekPos: Int) ->
        val apiType = getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)
        WeekyViewModel(
            dispatchers = get(),
            weekPos = weekPos,
            weeklyUseCase = get(named(UseCaseProperties.WEEKLY_USECASE)),
            hasFavoriteUseCase = HasFavoriteUseCase(get(), apiType)
        )
    }

    viewModel { (info: ToonInfo) ->
        val apiType = getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)
        EpisodeViewModel(
            dispatchers = get(),
            info = info,
            episodeUseCase = get(named(UseCaseProperties.EPISODE_USECASE)),
            readEpisodeListUseCase = ReadEpisodeListUseCase(get(), apiType),
            addFavoriteUseCase = AddFavoriteUseCase(get(), apiType),
            delFavoriteUseCase = RemoveFavoriteUseCase(get(), apiType)
        )
    }

    viewModel { (episode: EpisodeInfo) ->
        val apiType = getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)
        DetailViewModel(
            dispatchers = get(),
            episode = episode,
            detailUseCase = get(named(UseCaseProperties.DETAIL_USECASE)),
            readUseCase = ReadUseCase(get(), apiType),
            shareUseCase = get()
        )
    }
}

object ServiceProperties {
    const val NAV_ITEM = "NAV_ITEM"
}
