package com.pluu.webtoon.setting.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.pluu.webtoon.navigator.BrowserNavigator
import com.pluu.webtoon.setting.R
import com.pluu.webtoon.setting.licenseModels
import com.pluu.webtoon.setting.model.LicenseModel
import com.pluu.webtoon.ui.compose.activityComposeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * License Activity
 * Created by pluu on 2017-05-05.
 */
@AndroidEntryPoint
class LicenseActivity : ComponentActivity() {

    @Inject
    lateinit var browserNavigator: BrowserNavigator

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
        browserNavigator.openBrowser(
            this,
            ContextCompat.getColor(this, R.color.theme_primary),
            item.url
        )
    }
}
