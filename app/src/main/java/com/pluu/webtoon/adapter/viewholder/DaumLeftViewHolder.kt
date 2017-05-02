package com.pluu.webtoon.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pluu.webtoon.R
import com.pluu.webtoon.item.ChatView
import kotlinx.android.synthetic.main.view_chatting_left_layout.view.*

/**
 * 좌측 표시용 Item ViewHolder
 * Created by pluu on 2017-05-02.
 */
class DaumLeftViewHolder(v: View) : BaseChattingViewHolder(v) {

    override fun bind(context: Context, item: ChatView?) {
        if (item == null) return
        item.imgUrl?.let {
            loadProfileImage(context, itemView.leftProfileImageView, it)
        }
        itemView.leftNameTextView.text = item.name
        itemView.leftMessageTextView.text = item.text
    }

    companion object {
        fun newInstance(parent: ViewGroup) = DaumLeftViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_chatting_left_layout, parent, false))

    }
}