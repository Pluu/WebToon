package com.pluu.webtoon.ui.detail

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pluu.kotlin.inflate
import com.pluu.kotlin.screenWidth
import com.pluu.webtoon.R
import com.pluu.webtoon.item.DetailView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_detail_viewholder.*

class DetailAdapter(
    private val list: List<DetailView>,
    private val listener: ToggleListener?
) : RecyclerView.Adapter<DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(
            parent.inflate(
                R.layout.item_detail_viewholder,
                false
            )
        ).apply {
            itemView.setOnClickListener { listener?.childCallToggle(false) }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(list[position])
    }
}

class DetailViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    private val screenWidth = containerView.context.screenWidth()

    fun bind(item: DetailView) {
        Glide.with(itemView.context)
            .load(item.value)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(screenWidth, 0)
            )
            .into(ivView)
    }

}