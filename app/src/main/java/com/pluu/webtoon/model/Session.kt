package com.pluu.webtoon.model

import com.pluu.webtoon.data.pref.PrefConfig

class Session(
    prefConfig: PrefConfig
) {
    var navi = prefConfig.getDefaultWebToon()
}