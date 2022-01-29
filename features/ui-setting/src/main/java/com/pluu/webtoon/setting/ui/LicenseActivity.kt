package com.pluu.webtoon.setting.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.ColorInt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.webtoon.navigator.BrowserNavigator
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
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = isSystemInDarkTheme()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, !useDarkIcons)
            }

            val themeColor = MaterialTheme.colorScheme.primary.toArgb()
            ProvideWindowInsets {
                LicenseScreen(
                    list = licenseModels,
                    onBackPressed = { finish() },
                    onClicked = {
                        showDetailLicense(it, themeColor)
                    }
                )
            }
        }
    }

    /**
     * Show Detail License Page
     * @param item License Model
     */
    private fun showDetailLicense(item: LicenseModel, @ColorInt themeColor: Int) {
        browserNavigator.openBrowser(this, themeColor, item.url)
    }
}
