package com.pluu.webtoon.common

import com.pluu.webtoon.data.pref.PrefConfig
import javax.inject.Inject

class Session @Inject constructor(
    prefConfig: PrefConfig
) {
    var navi = prefConfig.getDefaultWebToon()
}