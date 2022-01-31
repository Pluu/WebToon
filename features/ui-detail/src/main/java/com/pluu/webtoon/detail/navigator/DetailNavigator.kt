package com.pluu.webtoon.detail.navigator

import android.content.Context
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.navigator.DetailNavigator
import com.pluu.webtoon.ui.model.PalletColor
import javax.inject.Inject

internal class DetailNavigatorImpl @Inject constructor() : DetailNavigator {
    override fun openDetail(
        context: Context,
        item: EpisodeInfo,
        palletColor: PalletColor
    ) {
//        context.startActivity<DetailActivity>(
//            Const.EXTRA_EPISODE to item,
//            Const.EXTRA_PALLET to palletColor
//        )
    }
}