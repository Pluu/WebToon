package com.pluu.webtoon.episode.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.episode.databinding.LayoutEpisodeListItemBinding
import com.pluu.webtoon.episode.ui.listener.EpisodeSelectListener
import com.pluu.webtoon.model.EpisodeInfo

/**
 * 에피소드 화면 Adapter
 * Created by pluu on 2017-05-02.
 */
internal class EpisodeAdapter(
    private val listener: EpisodeSelectListener
) : RecyclerView.Adapter<EpisodeViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutEpisodeListItemBinding.inflate(layoutInflater, parent, false)
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: EpisodeViewHolder, i: Int) {
        val position = viewHolder.absoluteAdapterPosition
        viewHolder.bind(list[position])
        viewHolder.clickThumbnail {
            if (list[position].isLock) {
                listener.selectLockItem()
            } else {
                listener.selectSuccess(list[position])
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
