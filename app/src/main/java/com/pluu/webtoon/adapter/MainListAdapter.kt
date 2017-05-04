package com.pluu.webtoon.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
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
open class MainListAdapter(mContext: Context, private val list: List<WebToonInfo>?, private val listener: WebToonSelectListener)
    : RecyclerView.Adapter<MainListAdapter.ViewHolder>() {

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
        var i = 0
        val size = list!!.size
        while (i < size) {
            val item = list[i]
            if (TextUtils.equals(info.toonId, item.toonId)) {
                item.isFavorite = info.isFavorite
                return i
            }
            i++
        }
        return -1
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
                    .centerCrop()
                    .error(R.drawable.ic_sentiment_very_dissatisfied_black_36dp)
                    .listener(object : RequestListener<String, GlideDrawable> {
                        override fun onException(e: Exception, model: String,
                                                 target: Target<GlideDrawable>,
                                                 isFirstResource: Boolean): Boolean {
                            itemView.progress.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: GlideDrawable, model: String,
                                                     target: Target<GlideDrawable>,
                                                     isFromMemoryCache: Boolean,
                                                     isFirstResource: Boolean): Boolean {
                            itemView.progress.visibility = View.GONE
                            return false
                        }
                    })
                    .into(itemView.thumbnailView)

            itemView.regDate.text = item.updateDate

            itemView.regDate.visibility = if (!TextUtils.isEmpty(item.updateDate)) View.VISIBLE else View.GONE

            itemView.tvUp.visibility = when (item.status) {
                Status.UPDATE -> View.VISIBLE
                Status.BREAK -> View.GONE
                else -> View.GONE
            }

            itemView.tvRest.visibility = when (item.status) {
                Status.UPDATE -> View.GONE
                Status.BREAK -> View.VISIBLE
                else -> View.GONE
            }

            itemView.tvNovel.visibility = if (item.type == WebToonType.NOVEL) View.VISIBLE else View.GONE
            itemView.tv19.visibility = if (item.isAdult) View.VISIBLE else View.GONE
            itemView.favorite.visibility = if (item.isFavorite) View.VISIBLE else View.GONE
        }
    }
}