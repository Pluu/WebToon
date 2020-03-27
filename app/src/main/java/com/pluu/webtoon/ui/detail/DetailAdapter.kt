package com.pluu.webtoon.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pluu.webtoon.databinding.ItemDetailViewholderBinding
import com.pluu.webtoon.domain.moel.DetailView
import com.pluu.webtoon.utils.loadUrlOriginal

class DetailAdapter(
    private val list: List<DetailView>,
    private val listener: ToggleListener?
) : RecyclerView.Adapter<DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDetailViewholderBinding.inflate(layoutInflater, parent, false)
        return DetailViewHolder(binding).apply {
            itemView.setOnClickListener { listener?.childCallToggle(false) }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(list[position])
    }
}

class DetailViewHolder(
    private val binding: ItemDetailViewholderBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DetailView) {
        binding.ivView.loadUrlOriginal(item.url)
    }
}
