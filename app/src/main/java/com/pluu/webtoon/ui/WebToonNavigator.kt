package com.pluu.webtoon.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.core.os.bundleOf
import com.pluu.utils.result.justSafeRegisterForActivityResult
import com.pluu.webtoon.AppNavigator
import com.pluu.webtoon.Const
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.episode.ui.EpisodesActivity
import com.pluu.webtoon.ui.model.PalletColor
import javax.inject.Inject

class WebToonNavigator @Inject constructor() : AppNavigator {
    override fun openEpisode(
        context: Context,
        caller: ActivityResultCaller,
        item: ToonInfo,
        palletColor: PalletColor,
        callback: (ActivityResult) -> Unit
    ) {
        caller.justSafeRegisterForActivityResult(
            Intent(context, EpisodesActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        Const.EXTRA_EPISODE to item,
                        Const.EXTRA_PALLET to palletColor
                    )
                )
            }, callback
        )
    }

    override fun openDetail(
        context: Context,
        caller: ActivityResultCaller,
        item: EpisodeInfo,
        palletColor: PalletColor,
        callback: (ActivityResult) -> Unit
    ) {
        caller.justSafeRegisterForActivityResult(
            Intent(context, com.pluu.webtoon.detail.ui.DetailActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        Const.EXTRA_EPISODE to item,
                        Const.EXTRA_PALLET to palletColor
                    )
                )
            }, callback
        )
    }
}