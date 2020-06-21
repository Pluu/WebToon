package com.pluu.webtoon.weekly

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.weekly.databinding.LayoutMainListItemBinding
import com.pluu.webtoon.weekly.listener.WebToonSelectListener

/**
 * Main EpisodeInfo List Adapter
 * Created by pluu on 2017-05-02.
 */
class WeeklyListAdapter(
    mContext: Context,
    private val list: List<ToonInfo>,
    private val listener: WebToonSelectListener
) : RecyclerView.Adapter<WeeklyViewHolder>() {
    private val filterColor: Int = ContextCompat.getColor(mContext, R.color.red_500)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): WeeklyViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = LayoutMainListItemBinding.inflate(layoutInflater, viewGroup, false)
        return WeeklyViewHolder(binding = binding, filterColor = filterColor)
    }

    override fun onBindViewHolder(viewHolder: WeeklyViewHolder, i: Int) {
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
