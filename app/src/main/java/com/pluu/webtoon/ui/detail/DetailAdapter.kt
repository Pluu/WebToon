package com.pluu.webtoon.ui.detail

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pluu.kotlin.inflate
import com.pluu.webtoon.R
import com.pluu.webtoon.item.DetailView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
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

    private val loader: Picasso = Picasso.get()

    fun bind(item: DetailView) {
        loader
            .load(item.value)
            .config(Bitmap.Config.RGB_565)
            .memoryPolicy(MemoryPolicy.NO_STORE)
            .into(ivView)
    }

}