package com.pluu.webtoon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pluu.kotlin.toVisibleOrGone
import com.pluu.webtoon.R
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.ui.listener.WebToonSelectListener
import com.pluu.webtoon.utils.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_main_list_item.*

/**
 * Main Episode List Adapter
 * Created by pluu on 2017-05-02.
 */
class MainListAdapter(
    mContext: Context,
    private val list: List<WebToonInfo>?,
    private val listener: WebToonSelectListener
) : RecyclerView.Adapter<MainListViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val filterColor: Int = ContextCompat.getColor(mContext, R.color.color_accent)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MainListViewHolder {
        val v = mInflater.inflate(R.layout.layout_main_list_item, viewGroup, false)
        return MainListViewHolder(v, filterColor)
    }

    override fun onBindViewHolder(viewHolder: MainListViewHolder, i: Int) {
        viewHolder.bind(list!![i])
        viewHolder.thumbnailView.setOnClickListener {
            if (list[i].isLock) {
                listener.selectLockItem()
            } else {
                listener.selectSuccess(viewHolder.thumbnailView, list[i])
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun modifyInfo(info: WebToonInfo): Int {
        val indexOfFirst =
            list?.indexOfFirst { item -> info.toonId == item.toonId } ?: -1
        if (indexOfFirst != -1) {
            list?.get(indexOfFirst)?.isFavorite = info.isFavorite
        }
        return indexOfFirst
    }
}

class MainListViewHolder(
    override val containerView: View, filterColor: Int
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        favorite.setColorFilter(filterColor)
    }

    fun bind(item: WebToonInfo) {
        titleView.tag = item
        titleView.text = item.title

        thumbnailView.loadUrl(item.image,
            ready = { progress.visibility = View.GONE },
            fail = { progress.visibility = View.GONE }
        )

        regDate.text = item.updateDate
        regDate.visibility = item.updateDate.isNullOrBlank().toVisibleOrGone()
        tvUp.visibility = (Status.UPDATE == item.status).toVisibleOrGone()
        tvRest.visibility = (Status.BREAK == item.status).toVisibleOrGone()
        tv19.visibility = item.isAdult.toVisibleOrGone()
        favorite.visibility = item.isFavorite.toVisibleOrGone()
    }
}