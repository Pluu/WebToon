package com.pluu.webtoon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.viewholder.EpisodeViewHolder
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.ui.listener.EpisodeSelectListener
import kotlinx.android.synthetic.main.layout_episode_list_item.view.*

/**
 * 에피소드 화면 Adapter
 * Created by pluu on 2017-05-02.
 */
open class EpisodeAdapter(val listener: EpisodeSelectListener) :
    RecyclerView.Adapter<EpisodeViewHolder>() {
    private val list = mutableListOf<EpisodeInfo>()

    fun addItems(list: List<EpisodeInfo>) {
        val size = this.list.size
        this.list.addAll(list)
        notifyItemRangeInserted(size, list.size)
    }

    fun getItem(position: Int): EpisodeInfo {
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

    fun updateRead(readList: List<String>) {
        for (id in readList) {
            for ((index, item) in list.withIndex()) {
                if (id == item.id && !item.isRead) {
                    // 읽음 표시하지 않은 item 만 변경 처리
                    item.isRead = true
                    notifyItemChanged(index)
                }
            }
        }
    }
}

