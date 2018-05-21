package com.pluu.webtoon.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.R
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.ui.listener.EpisodeSelectListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_episode_list_item.view.*
import java.lang.Exception

/**
 * 에피소드 화면 Adapter
 * Created by pluu on 2017-05-02.
 */
open class EpisodeAdapter(val listener: EpisodeSelectListener) :
    RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_episode_list_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
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

    fun updateRead(readList: MutableList<String?>) {
        for (id in readList) {
            for (item in list) {
                if (id == item.episodeId) {
                    item.setReadFlag()
                    break
                }
            }
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(item: Episode) {
            itemView.titleView.text = item.episodeTitle

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
