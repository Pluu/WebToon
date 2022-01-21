package com.pluu.webtoon.episode.sample

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.utils.startActivity
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.ui.EpisodesActivity
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.model.PalletColor

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
            darkVibrantColor = Color.DKGRAY,
            darkMutedColor = Color.LTGRAY,
            lightMutedColor = Color.WHITE
        )

        startActivity<EpisodesActivity>(
            Const.EXTRA_EPISODE to item,
            Const.EXTRA_PALLET to palletColor
        )
        finish()
    }
}
