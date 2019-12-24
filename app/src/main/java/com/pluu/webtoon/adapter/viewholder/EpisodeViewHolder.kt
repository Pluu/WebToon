package com.pluu.webtoon.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.R
import com.pluu.webtoon.databinding.LayoutEpisodeListItemBinding
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.utils.loadUrl
import kotlinx.android.extensions.LayoutContainer

class EpisodeViewHolder(
    private val binding: LayoutEpisodeListItemBinding,
    override val containerView: View = binding.root
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: EpisodeInfo) {
        binding.titleView.text = item.title

        binding.thumbnailView.loadUrl(item.image,
            ready = { binding.progress.visibility = View.GONE },
            fail = { binding.progress.visibility = View.GONE }
        )

        binding.readView.visibility = if (item.isRead) View.VISIBLE else View.GONE

        if (item.isLock) {
            binding.lockStatusView.setImageResource(R.drawable.lock_circle)
            binding.lockStatusView.visibility = View.VISIBLE
        } else {
            binding.lockStatusView.visibility = View.GONE
        }
    }

    fun clickThumbnail(action: () -> Unit) {
        binding.thumbnailView.setOnClickListener {
            action.invoke()
        }
    }
}
