package com.pluu.webtoon.common

import com.pluu.webtoon.data.PrefConfig
import javax.inject.Inject

class Session @Inject constructor(
    prefConfig: PrefConfig
) {
    var navi = prefConfig.getDefaultWebToon()
}