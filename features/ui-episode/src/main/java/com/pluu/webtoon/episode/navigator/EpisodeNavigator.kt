package com.pluu.webtoon.episode.navigator

import android.content.Context
import com.pluu.utils.startActivity
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.ui.EpisodesActivity
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.navigator.EpisodeNavigator
import com.pluu.webtoon.ui.model.PalletColor
import javax.inject.Inject

internal class EpisodeNavigatorImpl @Inject constructor() : EpisodeNavigator {
    override fun openEpisode(
        context: Context,
        item: ToonInfoWithFavorite,
        palletColor: PalletColor
    ) {
        context.startActivity<EpisodesActivity>(
            Const.EXTRA_EPISODE to item,
            Const.EXTRA_PALLET to palletColor
        )
    }
}