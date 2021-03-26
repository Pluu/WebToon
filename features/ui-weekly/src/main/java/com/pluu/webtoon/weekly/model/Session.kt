package com.pluu.webtoon.weekly.model

import com.pluu.webtoon.data.pref.PrefConfig
import javax.inject.Inject

class Session @Inject constructor(
    prefConfig: PrefConfig
) {
    var navi = prefConfig.getDefaultWebToon()
}