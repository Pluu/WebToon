package com.pluu.webtoon.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.pluu.utils.buildIntent
import com.pluu.utils.startActivity
import com.pluu.webtoon.AppNavigator
import com.pluu.webtoon.Const
import com.pluu.webtoon.detail.ui.DetailActivity
import com.pluu.webtoon.episode.ui.EpisodesActivity
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.setting.ui.LicenseActivity
import com.pluu.webtoon.setting.ui.SettingsActivity
import com.pluu.webtoon.ui.model.PalletColor
import javax.inject.Inject

class WebToonNavigator @Inject constructor() : AppNavigator {
    override fun openEpisode(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        item: ToonInfo,
        palletColor: PalletColor
    ) {
        val intent = context.buildIntent<EpisodesActivity>(
            Const.EXTRA_EPISODE to item,
            Const.EXTRA_PALLET to palletColor
        )
        launcher.launch(intent)
    }

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

    override fun openSetting(context: Context) {
        context.startActivity<SettingsActivity>()
    }

    override fun openLicense(context: Context) {
        context.startActivity<LicenseActivity>()
    }
}