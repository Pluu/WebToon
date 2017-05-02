package com.pluu.webtoon.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.pluu.webtoon.adapter.viewholder.*
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.VIEW_TYPE

/**
 * Detail, Chatting Adapter
 * Created by pluu on 2017-05-02.
 */
open class DetailChatAdapter(private val context: Context) : RecyclerView.Adapter<BaseChattingViewHolder>() {
    private val list = mutableListOf<DetailView>()

    fun setList(list: List<DetailView>) {
        this.list.addAll(list)
    }

    fun clear() {
        list.clear()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseChattingViewHolder {
        return when (VIEW_TYPE.values()[viewType]) {
            VIEW_TYPE.CHAT_NOTICE -> DaumNoticeViewHolder.newInstance(parent)
            VIEW_TYPE.CHAT_NOTICE_IMAGE -> DaumNoticeImageViewHolder.newInstance(parent)
            VIEW_TYPE.CHAT_LEFT -> DaumLeftViewHolder.newInstance(parent)
            VIEW_TYPE.CHAT_RIGHT -> DaumRIghtViewHolder.newInstance(parent)
            else -> DaumEmptyViewHolder.newInstance(parent)
        }
    }

    override fun onBindViewHolder(holder: BaseChattingViewHolder, position: Int) {
        val item = list[position]
        holder.bind(context, item.chatValue)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
