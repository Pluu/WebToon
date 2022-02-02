package com.pluu.webtoon.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.webtoon.model.CurrentSession
import com.pluu.webtoon.navigator.BrowserNavigator
import com.pluu.webtoon.ui.compose.WebToonTheme
import com.pluu.webtoon.weekly.model.toCoreType
import com.pluu.webtoon.weekly.model.toUiType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NavigationActivity : FragmentActivity() {
    @Inject
    lateinit var session: CurrentSession

    @Inject
    lateinit var browserNavigator: BrowserNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebToonContent()
        }
    }

    @Composable
    private fun WebToonContent() {
        val systemUiController = rememberSystemUiController()
        val isDarkTheme = isSystemInDarkTheme() // TODO: 화면마다 아이콘 노출 수정
        SideEffect {
            systemUiController.setSystemBarsColor(Color.Transparent, !isDarkTheme)
        }

        WebToonTheme(isDarkTheme) {
            val themeColor = MaterialTheme.colorScheme.primaryContainer.toArgb()
            var naviItem by rememberMutableStateOf(session.navi.toUiType())

            ProvideWindowInsets {
                AppNavigation(
                    naviItem = naviItem,
                    openBrowser = { url ->
                        browserNavigator.openBrowser(this, themeColor, url)
                    },
                    updateNaviItem = { item ->
                        session.navi = item.toCoreType()
                        naviItem = item
                    }
                )
            }
        }
    }
}
