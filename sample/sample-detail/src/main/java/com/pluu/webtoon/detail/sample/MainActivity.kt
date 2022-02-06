package com.pluu.webtoon.detail.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.webtoon.Const
import com.pluu.webtoon.detail.ui.compose.DetailUi
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.ui.compose.WebToonTheme
import com.pluu.webtoon.ui.model.PalletColor
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

            WebToonTheme(true) {
                ProvideWindowInsets {
                    Sample()
                }
            }
        }
    }

    @Composable
    fun Sample() {
        val item = EpisodeInfo(
            id = "1",
            toonId = "648419",
            title = "Test Detail",
            image = ""
        )

        val palletColor = PalletColor(
            darkVibrantColor = Color(0xFF17438F),
            darkMutedColor = Color.Gray,
            lightVibrantColor = Color.White,
            lightMutedColor = Color.White
        )

        // Put, Episode Information
        // Extra information saved here is included in SavedStateHandle of ViewModel.
        intent.putExtra(Const.EXTRA_EPISODE, item)

        DetailUi(
            palletColor = palletColor,
            closeCurrent = ::finish
        )
    }
}
