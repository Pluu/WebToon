package com.pluu.webtoon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.R
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.ui.listener.EpisodeSelectListener
import com.pluu.webtoon.utils.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_episode_list_item.*
import kotlinx.android.synthetic.main.layout_episode_list_item.view.*

/**
 * 에피소드 화면 Adapter
 * Created by pluu on 2017-05-02.
 */
open class EpisodeAdapter(val listener: EpisodeSelectListener) :
    RecyclerView.Adapter<EpisodeViewHolder>() {
    private val list = mutableListOf<Episode>()

    fun addItems(list: List<Episode>) {
        this.list.addAll(list)
    }

    fun getItem(position: Int): Episode {
        return list[position]
    }

    fun clear() {
        list.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_episode_list_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(viewHolder: EpisodeViewHolder, i: Int) {
        viewHolder.bind(list[i])
        viewHolder.itemView.thumbnailView?.setOnClickListener {
            if (list[viewHolder.adapterPosition].isLock) {
                listener.selectLockItem()
            } else {
                listener.selectSuccess(list[viewHolder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateRead(readList: MutableList<String?>) {
        for (id in readList) {
            for (item in list) {
                if (id == item.episodeId) {
                    item.setReadFlag()
                    break
                }
            }
        }
    }
}

class EpisodeViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: Episode) {
        titleView.text = item.episodeTitle

        thumbnailView.loadUrl(item.image,
            ready = { progress.visibility = View.GONE },
            fail = { progress.visibility = View.GONE }
        )

        readView.visibility = if (item.isReadFlag) View.VISIBLE else View.GONE

        if (item.isLock) {
            lockStatusView.setImageResource(R.drawable.lock_circle)
            lockStatusView.visibility = View.VISIBLE
        } else {
            lockStatusView.visibility = View.GONE
        }
    }
}
