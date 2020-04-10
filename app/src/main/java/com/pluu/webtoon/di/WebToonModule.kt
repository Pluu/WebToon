package com.pluu.webtoon.di

import androidx.lifecycle.SavedStateHandle
import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.domain.usecase.AddFavoriteUseCase
import com.pluu.webtoon.domain.usecase.HasFavoriteUseCase
import com.pluu.webtoon.domain.usecase.ReadEpisodeListUseCase
import com.pluu.webtoon.domain.usecase.ReadUseCase
import com.pluu.webtoon.domain.usecase.RemoveFavoriteUseCase
import com.pluu.webtoon.ui.detail.DetailViewModel
import com.pluu.webtoon.ui.episode.EpisodeViewModel
import com.pluu.webtoon.ui.intro.IntroViewModel
import com.pluu.webtoon.ui.weekly.ToonViewModel
import com.pluu.webtoon.ui.weekly.WeekyViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val introModule = module {
    viewModel {
        IntroViewModel()
    }
}

val webToonModule = module {
    viewModel { (weekPos: Int) ->
        val apiType = getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)
        WeekyViewModel(
            dispatchers = get(),
            type = apiType,
            weekPos = weekPos,
            weeklyUseCase = get(named(UseCaseProperties.WEEKLY_USECASE)),
            hasFavoriteUseCase = HasFavoriteUseCase(get())
        )
    }

    viewModel {
        ToonViewModel()
    }

    viewModel { (info: ToonInfo) ->
        val apiType = getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)
        EpisodeViewModel(
            dispatchers = get(),
            type = apiType,
            info = info,
            episodeUseCase = get(named(UseCaseProperties.EPISODE_USECASE)),
            readEpisodeListUseCase = ReadEpisodeListUseCase(get()),
            addFavoriteUseCase = AddFavoriteUseCase(get()),
            delFavoriteUseCase = RemoveFavoriteUseCase(get())
        )
    }

    viewModel { (handle: SavedStateHandle) ->
        val apiType = getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM)
        DetailViewModel(
            handle = handle,
            type = apiType,
            dispatchers = get(),
            detailUseCase = get(named(UseCaseProperties.DETAIL_USECASE)),
            readUseCase = ReadUseCase(get()),
            shareUseCase = get()
        )
    }
}

object ServiceProperties {
    const val NAV_ITEM = "NAV_ITEM"
}
