package com.pluu.webtoon.adapter.viewholder

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.utils.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_main_list_item.*

class MainListViewHolder(
    override val containerView: View,
    filterColor: Int
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        favorite.setColorFilter(filterColor)
    }

    fun bind(item: ToonInfo) {
        titleView.tag = item
        titleView.text = item.title

        thumbnailView.loadUrl(item.image,
            ready = { progress.visibility = View.GONE },
            fail = { progress.visibility = View.GONE }
        )

        regDate.text = item.updateDate
        regDate.isVisible = item.updateDate.isNotBlank()
        tvUp.isVisible = (Status.UPDATE == item.status)
        tvRest.isVisible = (Status.BREAK == item.status)
        tv19.isVisible = item.isAdult
        favorite.isVisible = item.isFavorite
    }
}
