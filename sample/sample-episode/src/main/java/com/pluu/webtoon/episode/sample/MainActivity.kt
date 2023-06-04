package com.pluu.webtoon.episode.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.utils.toast
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.ui.compose.EpisodeUi
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.compose.WebToonTheme
import com.pluu.webtoon.ui.compose.toLong
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
                Sample()
            }
        }
    }

    @Composable
    fun Sample() {
        val item = ToonInfoWithFavorite(
            info = ToonInfo(
                id = "648419",
                title = "Sample Title",
                image = "",
                writer = "작가",
                rate = 5.0
            )
        )
        val palletColor = PalletColor(
            darkVibrantColor = 0xFF17438F,
            darkMutedColor = Color.Gray.toLong(),
            lightVibrantColor = Color.White.toLong(),
            lightMutedColor = Color.White.toLong()
        )

        // Put, Episode Information
        // Extra information saved here is included in SavedStateHandle of ViewModel.
        intent.putExtra(Const.EXTRA_TOON, item)

        EpisodeUi(
            webToonItem = item,
            palletColor = palletColor,
            openDetail = {
                toast("Show Detail activity")
            },
            closeCurrent = ::finish
        )
    }
}
