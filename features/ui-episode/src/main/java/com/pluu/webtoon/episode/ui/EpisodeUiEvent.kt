package com.pluu.webtoon.episode.ui

import com.pluu.webtoon.model.EpisodeInfo

internal sealed class EpisodeUiEvent {
    data object OnShowFirst : EpisodeUiEvent()
    class OnShowDetail(val item: EpisodeInfo) : EpisodeUiEvent()
    data object OnBackPressed : EpisodeUiEvent()
}
