package com.pluu.webtoon.weekly.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.utils.toast
import com.pluu.webtoon.ui.compose.WebToonTheme
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import com.pluu.webtoon.weekly.ui.compose.WeeklyUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, false)
            }

            WebToonTheme(isSystemInDarkTheme()) {
                ProvideWindowInsets {
                    Sample()
                }
            }
        }
    }

    @Composable
    fun Sample() {
        WeeklyUi(
            naviItem = UI_NAV_ITEM.NAVER,
            onNavigateToMenu = {
                // TODO: reopen webtoon
            },
            openEpisode = { _, _ ->
                toast("Show Episode activity")
            },
            openSetting = {
                toast("Show Setting activity")
            }
        )
    }
}