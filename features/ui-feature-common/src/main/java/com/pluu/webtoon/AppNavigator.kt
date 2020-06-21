package com.pluu.webtoon

import android.content.Context
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.ui.model.PalletColor

interface AppNavigator {

    /** Weekly에서 에피소드 화면 선택 */
    fun openEpisode(
        context: Context,
        caller: ActivityResultCaller,
        item: ToonInfo,
        palletColor: PalletColor,
        callback: (ActivityResult) -> Unit
    )
}