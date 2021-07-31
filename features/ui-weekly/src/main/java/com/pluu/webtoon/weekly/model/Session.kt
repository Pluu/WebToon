package com.pluu.webtoon.weekly.model

import com.pluu.webtoon.data.pref.PrefConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Session @Inject constructor(
    prefConfig: PrefConfig
) {
    var navi = prefConfig.getDefaultWebToon()
}