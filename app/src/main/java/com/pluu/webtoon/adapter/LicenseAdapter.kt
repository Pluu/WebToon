package com.pluu.webtoon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pluu.event.EventBus
import com.pluu.webtoon.R
import com.pluu.webtoon.event.RecyclerViewEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Main EpisodeInfo List Adapter
 * Created by pluu on 2017-05-02.
 */
class LicenseAdapter(context: Context) : RecyclerView.Adapter<LicenseAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val list = context.resources.getStringArray(R.array.license_title)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = mInflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false)
        return ViewHolder(v)
    }

    @ExperimentalCoroutinesApi
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.text1.text = list[i]
        viewHolder.itemView.setOnClickListener {
            EventBus.send(RecyclerViewEvent(viewHolder.absoluteAdapterPosition))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val text1: TextView = v.findViewById(android.R.id.text1)
    }
}
