package com.pluu.webtoon

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.ui.model.PalletColor

interface AppNavigator {

    /** Weekly 에서 Episode 화면 선택 */
    fun openEpisode(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        item: ToonInfo,
        palletColor: PalletColor
    )

    /** Episode 에서 Detail 화면 선택 */
    fun openDetail(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        item: EpisodeInfo,
        palletColor: PalletColor
    )

    fun openSetting(context: Context)

    fun openLicense(context: Context)
}