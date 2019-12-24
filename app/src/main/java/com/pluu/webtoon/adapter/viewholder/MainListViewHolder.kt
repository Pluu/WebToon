package com.pluu.webtoon.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.databinding.LayoutMainListItemBinding
import com.pluu.webtoon.domain.moel.Status
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.utils.loadUrl
import kotlinx.android.extensions.LayoutContainer

class MainListViewHolder(
    private val binding: LayoutMainListItemBinding,
    override val containerView: View = binding.root,
    filterColor: Int
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        binding.favorite.setColorFilter(filterColor)
    }

    fun bind(item: ToonInfo) {
        binding.titleView.tag = item
        binding.titleView.text = item.title

        binding.thumbnailView.loadUrl(item.image,
            ready = { binding.progress.visibility = View.GONE },
            fail = { binding.progress.visibility = View.GONE }
        )

        binding.regDate.text = item.updateDate
        binding.regDate.isVisible = item.updateDate.isNotBlank()
        binding.tvUp.isVisible = (Status.UPDATE == item.status)
        binding.tvRest.isVisible = (Status.BREAK == item.status)
        binding.tv19.isVisible = item.isAdult
        binding.favorite.isVisible = item.isFavorite
    }

    fun clickThumbnail(action: (ImageView) -> Unit) {
        binding.thumbnailView.setOnClickListener {
            action.invoke(it as ImageView)
        }
    }
}
