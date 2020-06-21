package com.pluu.webtoon.episode.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.episode.databinding.LayoutEpisodeListItemBinding
import com.pluu.webtoon.utils.loadUrl

class EpisodeViewHolder(
    private val binding: LayoutEpisodeListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

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
