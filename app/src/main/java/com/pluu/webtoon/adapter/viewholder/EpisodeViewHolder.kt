package com.pluu.webtoon.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.R
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.utils.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_episode_list_item.*

class EpisodeViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: EpisodeInfo) {
        titleView.text = item.title

        thumbnailView.loadUrl(item.image,
            ready = { progress.visibility = View.GONE },
            fail = { progress.visibility = View.GONE }
        )

        readView.visibility = if (item.isRead) View.VISIBLE else View.GONE

        if (item.isLock) {
            lockStatusView.setImageResource(R.drawable.lock_circle)
            lockStatusView.visibility = View.VISIBLE
        } else {
            lockStatusView.visibility = View.GONE
        }
    }
}
