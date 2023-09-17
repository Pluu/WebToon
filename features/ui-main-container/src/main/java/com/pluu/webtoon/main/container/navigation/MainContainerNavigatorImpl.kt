package com.pluu.webtoon.main.container.navigation

import android.content.Context
import com.pluu.utils.startActivity
import com.pluu.webtoon.main.container.ui.NavigationActivity
import com.pluu.webtoon.navigator.MainContainerNavigator
import javax.inject.Inject

internal class MainContainerNavigatorImpl @Inject constructor() : MainContainerNavigator {
    override fun openWebToonContainer(context: Context) {
        context.startActivity<NavigationActivity>()
    }
}