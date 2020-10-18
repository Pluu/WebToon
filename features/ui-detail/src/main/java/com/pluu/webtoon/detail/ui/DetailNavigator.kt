package com.pluu.webtoon.detail.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.pluu.utils.buildIntent
import com.pluu.webtoon.Const
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.navigator.DetailNavigator
import com.pluu.webtoon.ui.model.PalletColor
import javax.inject.Inject

class DetailNavigatorImpl @Inject constructor() : DetailNavigator {
    override fun openDetail(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        item: EpisodeInfo,
        palletColor: PalletColor
    ) {
        val intent = context.buildIntent<DetailActivity>(
            Const.EXTRA_EPISODE to item,
            Const.EXTRA_PALLET to palletColor
        )
        launcher.launch(intent)
    }
}