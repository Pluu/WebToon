package com.pluu.webtoon.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.pluu.webtoon.R
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.VIEW_TYPE
import kotlinx.android.synthetic.main.view_multi_list_item.view.*

/**
 * Detail, Multi Adapter
 * Created by pluu on 2017-05-02.
 */
open class DetailMultiAdapter(context: Context) : RecyclerView.Adapter<DetailMultiAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val list = mutableListOf<DetailView>()

    fun setList(list: List<DetailView>) {
        this.list.addAll(list)
    }

    fun clear() {
        list.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.view_multi_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(item: DetailView) = with(itemView) {
            val options = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
            when (item.type) {
                VIEW_TYPE.MULTI_IMAGE -> {
                    itemView.emptyView.visibility = View.GONE
                    if (item.height != 0f) {
                        itemView.imageView.sethRatio(item.height)
                        Glide.with(context)
                                .load(item.value)
                                .apply(options)
                                .into(itemView.imageView)
                    } else {
                        Glide.with(context)
                                .asBitmap()
                                .load(item.value)
                                .apply(options)
                                .into(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                        item.height = resource.height.toFloat() / resource.width
                                        itemView.imageView.sethRatio(item.height)
                                        itemView.imageView.setImageBitmap(resource)
                                    }
                                })
                    }
                }
                VIEW_TYPE.MULTI_GIF -> {
                    itemView.emptyView.visibility = View.GONE
                    if (item.height != 0f) {
                        itemView.imageView.sethRatio(item.height)
                        Glide.with(context)
                                .load(item.value)
                                .apply(options)
                                .into(itemView.imageView)
                    } else {
                        Glide.with(context)
                                .asGif()
                                .load(item.value)
                                .apply(options)
                                .into(object : SimpleTarget<GifDrawable>() {
                                    override fun onResourceReady(resource: GifDrawable, transition: Transition<in GifDrawable>?) {
                                        item.height = resource.intrinsicHeight.toFloat() / resource.intrinsicWidth
                                        itemView.imageView.sethRatio(item.height)
                                        itemView.imageView.setImageDrawable(resource)
                                    }
                                })
                    }
                }
                else -> {
                    itemView.imageView.visibility = View.GONE
                    itemView.emptyView.visibility = View.VISIBLE
                }
            }
        }
    }
}
