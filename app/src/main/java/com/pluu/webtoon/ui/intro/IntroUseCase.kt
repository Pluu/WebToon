package com.pluu.webtoon.ui.intro

import android.content.Context
import com.pluu.webtoon.common.PrefConfig
import com.pluu.webtoon.di.ServiceProperties
import org.koin.standalone.KoinComponent
import org.koin.standalone.setProperty

class IntroUseCase(
    private val context: Context
) : KoinComponent {

    fun init() {
        setProperty(ServiceProperties.NAV_ITEM, PrefConfig.getDefaultWebToon(context))
    }
}
