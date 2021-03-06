package com.pluu.webtoon.episode.ui

import com.pluu.webtoon.model.EpisodeInfo

sealed class EpisodeUiEvent {
    class OnShowFirst(val item: EpisodeInfo) : EpisodeUiEvent()
    class OnShowDetail(val item: EpisodeInfo) : EpisodeUiEvent()
    object OnBackPressed : EpisodeUiEvent()
    class UpdateFavorite(val isFavorite: Boolean) : EpisodeUiEvent()
    object MoreLoad : EpisodeUiEvent()
}
