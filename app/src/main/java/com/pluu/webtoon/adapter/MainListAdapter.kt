package com.pluu.webtoon.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pluu.kotlin.toVisibleOrGone
import com.pluu.webtoon.R
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.item.WebToonType
import com.pluu.webtoon.ui.listener.WebToonSelectListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_main_list_item.view.*
import java.lang.Exception

/**
 * Main Episode List Adapter
 * Created by pluu on 2017-05-02.
 */
class MainListAdapter(
    mContext: Context,
    private val list: List<WebToonInfo>?,
    private val listener: WebToonSelectListener
) : RecyclerView.Adapter<MainListAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val filterColor: Int = ContextCompat.getColor(mContext, R.color.color_accent)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = mInflater.inflate(R.layout.layout_main_list_item, viewGroup, false)
        return ViewHolder(v, filterColor)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(list!![i])
        viewHolder.itemView.thumbnailView?.setOnClickListener {
            if (list[i].isLock) {
                listener.selectLockItem()
            } else {
                listener.selectSuccess(viewHolder.itemView.thumbnailView, list[i])
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

    class ViewHolder(v: View, filterColor: Int) : RecyclerView.ViewHolder(v) {
        init {
            itemView.favorite.setColorFilter(filterColor)
        }

        fun bind(item: WebToonInfo) {
            itemView.titleView.tag = item
            itemView.titleView.text = item.title

            Picasso.get()
                .load(item.image)
                .config(Bitmap.Config.RGB_565)
                .error(R.drawable.ic_sentiment_very_dissatisfied_black_36dp)
                .into(itemView.thumbnailView, object : Callback {
                    override fun onSuccess() {
                        itemView.progress.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        itemView.progress.visibility = View.GONE
                    }
                })

            itemView.regDate.text = item.updateDate
            itemView.regDate.visibility = item.updateDate.isNullOrBlank().toVisibleOrGone()
            itemView.tvUp.visibility = (Status.UPDATE == item.status).toVisibleOrGone()
            itemView.tvRest.visibility = (Status.BREAK == item.status).toVisibleOrGone()
            itemView.tvNovel.visibility = (item.type == WebToonType.NOVEL).toVisibleOrGone()
            itemView.tv19.visibility = item.isAdult.toVisibleOrGone()
            itemView.favorite.visibility = item.isFavorite.toVisibleOrGone()
        }
    }
}