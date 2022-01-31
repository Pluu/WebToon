package com.pluu.webtoon.episode.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.utils.toast
import com.pluu.webtoon.episode.ui.compose.EpisodeUi
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.compose.WebToonTheme
import com.pluu.webtoon.ui.model.PalletColor
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            darkVibrantColor = Color(0xFF17438F),
            darkMutedColor = Color.Gray,
            lightMutedColor = Color.White
        )

        startActivity<EpisodesActivity>(
            Const.EXTRA_EPISODE to item,
            Const.EXTRA_PALLET to palletColor
        )
        finish()
    }
}
