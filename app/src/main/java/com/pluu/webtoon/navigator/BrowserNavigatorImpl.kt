package com.pluu.webtoon.navigator

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import javax.inject.Inject

class BrowserNavigatorImpl @Inject constructor() : BrowserNavigator {
    override fun openBrowser(context: Context, toolbarColor: Int, url: String) {
        // http://qiita.com/droibit/items/66704f96a602adec5a35
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(toolbarColor)
                    .build()
            )
            .build()
            .launchUrl(context, Uri.parse(url))
    }
}