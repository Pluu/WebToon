package com.pluu.webtoon.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pluu.event.RxBusProvider
import com.pluu.webtoon.R
import com.pluu.webtoon.event.RecyclerViewEvent

/**
 * Main Episode List Adapter
 * Created by pluu on 2017-05-02.
 */
class LicenseAdapter(context: Context) : RecyclerView.Adapter<LicenseAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val list = context.resources.getStringArray(R.array.license_title)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = mInflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.text1.text = list[i]
        viewHolder.itemView.setOnClickListener {
            RxBusProvider.getInstance().send(RecyclerViewEvent(viewHolder.adapterPosition))
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val text1: TextView = v.findViewById(android.R.id.text1) as TextView
    }
}