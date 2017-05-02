package com.pluu.webtoon.adapter.viewholder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pluu.webtoon.R
import com.pluu.webtoon.item.ChatView

/**
 * Base Chatting ViewHolder
 * Created by pluu on 2017-05-02.
 */
abstract class BaseChattingViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    protected fun loadProfileImage(context: Context, view: ImageView, url: String) {
        val profileSize = context.resources.getDimensionPixelSize(R.dimen.chatting_profile_size)

        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(profileSize, profileSize)
                .centerCrop()
                .placeholder(R.drawable.transparent_background)
                .into(view)
    }

    abstract fun bind(context: Context, item: ChatView?)
}