package com.pluu.webtoon.episode.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.pluu.utils.buildIntent
import com.pluu.webtoon.Const
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.navigator.EpisodeNavigator
import com.pluu.webtoon.ui.model.PalletColor
import javax.inject.Inject

class EpisodeNavigatorImpl @Inject constructor() : EpisodeNavigator {
    override fun openEpisode(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        item: ToonInfoWithFavorite,
        palletColor: PalletColor
    ) {
        val intent = context.buildIntent<EpisodesActivity>(
            Const.EXTRA_EPISODE to item,
            Const.EXTRA_PALLET to palletColor
        )
        launcher.launch(intent)
    }
}