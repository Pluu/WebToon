package com.pluu.webtoon

import android.content.Context
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.ui.model.PalletColor

interface AppNavigator {

    /** Weekly 에서 Episode 화면 선택 */
    fun openEpisode(
        context: Context,
        caller: ActivityResultCaller,
        item: ToonInfo,
        palletColor: PalletColor,
        callback: (ActivityResult) -> Unit
    )

    /** Episode 에서 Detail 화면 선택 */
    fun openDetail(
        context: Context,
        caller: ActivityResultCaller,
        item: EpisodeInfo,
        palletColor: PalletColor,
        callback: (ActivityResult) -> Unit
    )

    fun openSetting(context: Context)

    fun openLicense(context: Context)
}