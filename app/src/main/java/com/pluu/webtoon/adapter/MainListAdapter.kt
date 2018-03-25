package com.pluu.webtoon.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.pluu.webtoon.R
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.item.WebToonType
import com.pluu.webtoon.ui.listener.WebToonSelectListener
import kotlinx.android.synthetic.main.layout_main_list_item.view.*

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
        viewHolder.itemView?.thumbnailView?.setOnClickListener {
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
            list?.indexOfFirst { item -> TextUtils.equals(info.toonId, item.toonId) } ?: -1
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
            Glide.with(itemView.context)
                .load(item.image)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .error(R.drawable.ic_sentiment_very_dissatisfied_black_36dp)
                )
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemView.progress.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemView.progress.visibility = View.GONE
                        return false
                    }
                })
                .into(itemView.thumbnailView)

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