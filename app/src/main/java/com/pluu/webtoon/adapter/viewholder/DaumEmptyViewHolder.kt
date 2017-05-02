package com.pluu.webtoon.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pluu.webtoon.R

import com.pluu.webtoon.item.ChatView

/**
 * 일부 간격 Divider Item ViewHolder
 * Created by pluu on 2017-05-02.
 */
class DaumEmptyViewHolder(v: View) : BaseChattingViewHolder(v) {

    override fun bind(context: Context, item: ChatView?) {}

    companion object {
        fun newInstance(parent: ViewGroup) = DaumEmptyViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_chatting_empty_layout, parent, false))
    }

}
