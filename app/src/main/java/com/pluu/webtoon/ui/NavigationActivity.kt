package com.pluu.webtoon.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.webtoon.model.CurrentSession
import com.pluu.webtoon.navigation.customtabs.rememberWebToonNavController
import com.pluu.webtoon.ui.compose.WebToonTheme
import com.pluu.webtoon.weekly.model.toCoreType
import com.pluu.webtoon.weekly.model.toUiType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NavigationActivity : FragmentActivity() {
    @Inject
    lateinit var session: CurrentSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            WebToonContent()
        }
    }

    @Composable
    private fun WebToonContent() {
        val systemUiController = rememberSystemUiController()
        val isDarkTheme = isSystemInDarkTheme() // TODO: 화면마다 아이콘 노출 수정
        val navController = rememberWebToonNavController()

        SideEffect {
            systemUiController.setSystemBarsColor(Color.Transparent, false)
            systemUiController.setNavigationBarColor(Color.Transparent, false)
        }

        WebToonTheme(isDarkTheme) {
            var naviItem by rememberMutableStateOf(session.navi.toUiType())

            AppNavigation(
                navController = navController,
                naviItem = naviItem,
                updateNaviItem = { item ->
                    session.navi = item.toCoreType()
                    naviItem = item
                }
            )
        }
    }
}
