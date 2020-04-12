package com.pluu.webtoon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.viewholder.MainListViewHolder
import com.pluu.webtoon.databinding.LayoutMainListItemBinding
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.ui.listener.WebToonSelectListener

/**
 * Main EpisodeInfo List Adapter
 * Created by pluu on 2017-05-02.
 */
class MainListAdapter(
    mContext: Context,
    private val list: List<ToonInfo>,
    private val listener: WebToonSelectListener
) : RecyclerView.Adapter<MainListViewHolder>() {
    private val filterColor: Int = ContextCompat.getColor(mContext, R.color.red_500)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MainListViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = LayoutMainListItemBinding.inflate(layoutInflater, viewGroup, false)
        return MainListViewHolder(binding = binding, filterColor = filterColor)
    }

    override fun onBindViewHolder(viewHolder: MainListViewHolder, i: Int) {
        viewHolder.bind(list[i])
        viewHolder.clickThumbnail {
            if (list[i].isLock) {
                listener.selectLockItem()
            } else {
                listener.selectSuccess(it, list[i])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun modifyInfo(id: String, isFavorite: Boolean) {
        val index = list.indexOfFirst { item ->
            id == item.id
        }.takeIf {
            it > -1
        } ?: return

        list[index].isFavorite = isFavorite
        notifyItemChanged(index)
    }
}
