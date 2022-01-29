package com.pluu.webtoon.setting.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.compose.ambient.ProvidePreference
import com.pluu.utils.startActivity
import com.pluu.webtoon.ui.compose.activityComposeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        activityComposeView {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent)
            }

            ProvideWindowInsets {
                ProvidePreference(sharedPreferences) {
                    SettingsScreen(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        onBackPressed = ::finish,
                        onOpenSourceClicked = ::openLicense
                    )
                }
            }
        }
    }

    private fun openLicense() {
        startActivity<LicenseActivity>()
    }
}