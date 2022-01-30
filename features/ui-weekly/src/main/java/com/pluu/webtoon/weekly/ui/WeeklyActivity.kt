package com.pluu.webtoon.weekly.ui

import android.os.Bundle
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.webtoon.model.CurrentSession
import com.pluu.webtoon.navigator.EpisodeNavigator
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.ui.compose.WebToonTheme
import com.pluu.webtoon.ui.compose.activityComposeView
import com.pluu.webtoon.ui.compose.navigator.LocalEpisodeNavigator
import com.pluu.webtoon.weekly.model.getCoreType
import com.pluu.webtoon.weekly.model.toUiType
import com.pluu.webtoon.weekly.ui.compose.WeeklyUi
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * 주간 화면 Activity
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class WeeklyActivity : FragmentActivity() {

    @Inject
    lateinit var session: CurrentSession

    @Inject
    lateinit var episodeNavigator: EpisodeNavigator

    @Inject
    lateinit var settingNavigator: SettingNavigator

    @Inject
    lateinit var dayViewModelFactory: WeeklyDayViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        activityComposeView {
            val systemUiController = rememberSystemUiController()
            val isDarkTheme = isSystemInDarkTheme()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
            }

            WebToonTheme(useDarkColors = isDarkTheme) {
                ProvideWindowInsets(false) {
                    CompositionLocalProvider(
                        LocalEpisodeNavigator provides episodeNavigator
                    ) {
                        WeeklyContent()
                    }
                }
            }
        }
    }

    @Composable
    private fun WeeklyContent() {
        var naviItem by rememberMutableStateOf(session.navi.toUiType())
        key(naviItem) {
            WeeklyUi(
                naviItem = naviItem,
                dayViewModelFactory = dayViewModelFactory,
                onNavigateToMenu = { item ->
                    if (naviItem != item) {
                        Timber.d(item.name)
                        session.navi = item.getCoreType()
                        naviItem = item
                    }
                },
                onNavigateToSetting = {
                    settingNavigator.openSetting(this)
                }
            )
        }
    }
}
