package com.pluu.webtoon.adapter

import android.content.Context
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
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.ui.listener.EpisodeSelectListener
import kotlinx.android.synthetic.main.layout_episode_list_item.view.*

/**
 * 에피소드 화면 Adapter
 * Created by pluu on 2017-05-02.
 */
open class EpisodeAdapter(mContext: Context, val listener: EpisodeSelectListener) : RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int) =
        ViewHolder(mInflater.inflate(R.layout.layout_episode_list_item, viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(list[i])
        viewHolder.itemView?.thumbnailView?.setOnClickListener {
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
                if (TextUtils.equals(id, item.episodeId)) {
                    item.setReadFlag()
                    break
                }
            }
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(item: Episode) {
            itemView.titleView.text = item.episodeTitle

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

            itemView.readView.visibility = if (item.isReadFlag) View.VISIBLE else View.GONE

            if (item.isLock) {
                itemView.lockStatusView.setImageResource(R.drawable.lock_circle)
                itemView.lockStatusView.visibility = View.VISIBLE
            } else {
                itemView.lockStatusView.visibility = View.GONE
            }
        }
    }
}
