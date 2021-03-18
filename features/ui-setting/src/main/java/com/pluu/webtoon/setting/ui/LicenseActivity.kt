package com.pluu.webtoon.setting.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.pluu.webtoon.setting.R
import com.pluu.webtoon.setting.licenseModels
import com.pluu.webtoon.setting.model.LicenseModel
import com.pluu.webtoon.ui.compose.activityComposeView

/**
 * License Activity
 * Created by pluu on 2017-05-05.
 */
class LicenseActivity : ComponentActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        activityComposeView {
            ProvideWindowInsets {
                LicenseScreen(
                    list = licenseModels,
                    onBackPressed = { finish() },
                    onClicked = ::showDetailLicense
                )
            }
        }
    }

    /**
     * Show Detail License Page
     * @param item License Model
     */
    private fun showDetailLicense(item: LicenseModel) {
        // http://qiita.com/droibit/items/66704f96a602adec5a35
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(ContextCompat.getColor(this, R.color.theme_primary))
                    .build()
            )
            .build()
            .launchUrl(this, Uri.parse(item.url))
    }
}
