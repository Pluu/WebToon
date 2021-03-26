package com.pluu.webtoon.ui.compose.navigator

import androidx.compose.runtime.compositionLocalOf
import com.pluu.webtoon.navigator.EpisodeNavigator

val LocalEpisodeNavigator = compositionLocalOf<EpisodeNavigator> {
    error("LocalEpisodeNavigator value not available. Are you using EpisodeNavigator?")
}