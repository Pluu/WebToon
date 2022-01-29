package com.pluu.webtoon.detail.sample

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.utils.startActivity
import com.pluu.webtoon.Const
import com.pluu.webtoon.detail.ui.DetailActivity
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.ui.model.PalletColor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = EpisodeInfo(
            id = "1",
            toonId = "648419",
            title = "Test Detail",
            image = ""
        )

        val palletColor = PalletColor(
            darkVibrantColor = 0xFF17438F.toInt(),
            darkMutedColor = Color.GRAY,
            lightMutedColor = Color.WHITE
        )

        startActivity<DetailActivity>(
            Const.EXTRA_EPISODE to item,
            Const.EXTRA_PALLET to palletColor
        )
        finish()
    }
}
