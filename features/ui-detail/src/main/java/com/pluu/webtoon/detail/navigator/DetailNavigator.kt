package com.pluu.webtoon.detail.navigator

import android.content.Context
import com.pluu.utils.startActivity
import com.pluu.webtoon.Const
import com.pluu.webtoon.detail.ui.DetailActivity
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.navigator.DetailNavigator
import com.pluu.webtoon.ui.model.PalletColor
import javax.inject.Inject

class DetailNavigatorImpl @Inject constructor() : DetailNavigator {
    override fun openDetail(
        context: Context,
        item: EpisodeInfo,
        palletColor: PalletColor
    ) {
        context.startActivity<DetailActivity>(
            Const.EXTRA_EPISODE to item,
            Const.EXTRA_PALLET to palletColor
        )
    }
}