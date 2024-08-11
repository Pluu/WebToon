package com.pluu.webtoon.main.container.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.webtoon.main.container.navigator.customtabs.rememberWebToonNavController
import com.pluu.webtoon.model.CurrentSession
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
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.TRANSPARENT
            )
        )

        setContent {
            WebToonContent {
                updateTheme(it)
            }
        }
    }

    private fun updateTheme(isForceDark: Boolean) {
        enableEdgeToEdge(
            statusBarStyle = if (isForceDark) {
                SystemBarStyle.dark(Color.TRANSPARENT)
            } else {
                SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
            }
        )
    }

    @Composable
    private fun WebToonContent(
        updateTheme: (Boolean) -> Unit
    ) {
        val navController = rememberWebToonNavController()
        WebToonTheme {
            var naviItem by rememberMutableStateOf(session.navi.toUiType())

            AppNavigation(
                navController = navController,
                naviItem = naviItem,
                updateNaviItem = { item ->
                    session.navi = item.toCoreType()
                    naviItem = item
                },
                updateTheme = updateTheme
            )
        }
    }
}
