package com.pluu.webtoon.ui.intro

import android.content.Context
import com.pluu.webtoon.common.PrefConfig
import com.pluu.webtoon.di.Property
import org.koin.standalone.KoinComponent
import org.koin.standalone.setProperty

class IntroUseCase(
    private val context: Context
) : KoinComponent {

    fun init() {
        setProperty(Property.NAV_ITEM_KEY, PrefConfig.getDefaultWebToon(context))
    }
}
