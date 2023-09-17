package com.pluu.webtoon.sample

import android.content.Context
import com.pluu.utils.toast
import com.pluu.webtoon.navigator.MainContainerNavigator

internal class BaseSampleNavigator : MainContainerNavigator {

    override fun openWebToonContainer(context: Context) {
        context.toast("Show Weekly activity")
    }

}