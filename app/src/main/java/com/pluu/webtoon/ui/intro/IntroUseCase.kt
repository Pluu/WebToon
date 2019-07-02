package com.pluu.webtoon.ui.intro

import com.pluu.webtoon.common.PrefConfig
import com.pluu.webtoon.di.ServiceProperties
import org.koin.core.KoinComponent

class IntroUseCase(
    private val pref: PrefConfig
) : KoinComponent {

    fun init() {
        getKoin().setProperty(ServiceProperties.NAV_ITEM, pref.getDefaultWebToon())
    }
}
