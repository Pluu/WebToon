package com.pluu.webtoon.navigator

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.model.PalletColor

interface WeeklyNavigator {
    /** Weekly 화면으로 이동 */
    fun openWeekly(context: Context)
}

interface EpisodeNavigator {
    /** Weekly 에서 Episode 화면 선택 */
    fun openEpisode(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        item: ToonInfoWithFavorite,
        palletColor: PalletColor
    )
}

interface DetailNavigator {
    /** Episode 에서 Detail 화면 선택 */
    fun openDetail(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        item: EpisodeInfo,
        palletColor: PalletColor
    )
}

interface SettingNavigator {
    fun openSetting(context: Context)

    fun openLicense(context: Context)
}
