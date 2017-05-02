package com.pluu.webtoon.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pluu.webtoon.R
import com.pluu.webtoon.item.ChatView
import kotlinx.android.synthetic.main.view_chatting_notice_layout.view.*

/**
 * 공지 텍스트용 Item ViewHolder
 * Created by  pluu on 2017-05-02.
 */
class DaumNoticeViewHolder(v: View) : BaseChattingViewHolder(v) {

    override fun bind(context: Context, item: ChatView?) {
        if (item == null) return
        itemView.text1.text = item.text
    }

    companion object {
        fun newInstance(parent: ViewGroup) = DaumNoticeViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_chatting_notice_layout, parent, false))
    }
}
