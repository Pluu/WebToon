package com.pluu.webtoon.ui

import android.content.Context
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import com.pluu.utils.buildIntent
import com.pluu.utils.result.justSafeRegisterForActivityResult
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
        caller: ActivityResultCaller,
        item: ToonInfo,
        palletColor: PalletColor,
        callback: (ActivityResult) -> Unit
    ) {
        val intent = context.buildIntent<EpisodesActivity>(
            Const.EXTRA_EPISODE to item,
            Const.EXTRA_PALLET to palletColor
        )
        caller.justSafeRegisterForActivityResult(intent, callback)
    }

    override fun openDetail(
        context: Context,
        caller: ActivityResultCaller,
        item: EpisodeInfo,
        palletColor: PalletColor,
        callback: (ActivityResult) -> Unit
    ) {
        val intent = context.buildIntent<DetailActivity>(
            Const.EXTRA_EPISODE to item,
            Const.EXTRA_PALLET to palletColor
        )
        caller.justSafeRegisterForActivityResult(intent, callback)
    }

    override fun openSetting(context: Context) {
        context.startActivity<SettingsActivity>()
    }

    override fun openLicense(context: Context) {
        context.startActivity<LicenseActivity>()
    }
}