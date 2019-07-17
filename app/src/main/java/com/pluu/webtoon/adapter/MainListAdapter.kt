package com.pluu.webtoon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.viewholder.MainListViewHolder
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.ui.listener.WebToonSelectListener
import kotlinx.android.synthetic.main.layout_main_list_item.*

/**
 * Main EpisodeInfo List Adapter
 * Created by pluu on 2017-05-02.
 */
class MainListAdapter(
    mContext: Context,
    private val list: List<ToonInfo>,
    private val listener: WebToonSelectListener
) : RecyclerView.Adapter<MainListViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val filterColor: Int = ContextCompat.getColor(mContext, R.color.color_accent)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MainListViewHolder {
        val v = mInflater.inflate(R.layout.layout_main_list_item, viewGroup, false)
        return MainListViewHolder(v, filterColor)
    }

    override fun onBindViewHolder(viewHolder: MainListViewHolder, i: Int) {
        viewHolder.bind(list[i])
        viewHolder.thumbnailView.setOnClickListener {
            if (list[i].isLock) {
                listener.selectLockItem()
            } else {
                listener.selectSuccess(viewHolder.thumbnailView, list[i])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun modifyInfo(id: String, isFavorite: Boolean) {
        list.indexOfFirst { item ->
            id == item.id
        }.takeIf {
            it > -1
        }?.let { index ->
            list[index].isFavorite = isFavorite
            notifyItemChanged(index)
        }
    }
}

