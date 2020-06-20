package com.pluu.webtoon.common

import com.pluu.webtoon.data.pref.PrefConfig

class Session(
    prefConfig: PrefConfig
) {
    var navi = prefConfig.getDefaultWebToon()
}