package com.pluu.webtoon.episode.ui.compose

import com.pluu.webtoon.model.EpisodeInfo

sealed class EpisodeUiEvent {
    object OnShowFirst : EpisodeUiEvent()
    class OnShowDetail(val item: EpisodeInfo) : EpisodeUiEvent()
    object OnBackPressed : EpisodeUiEvent()
    class UpdateFavorite(val isFavorite: Boolean) : EpisodeUiEvent()
    object MoreLoad : EpisodeUiEvent()
}
