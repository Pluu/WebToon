package com.pluu.webtoon.adapter.viewholder

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.pluu.webtoon.R
import com.pluu.webtoon.item.ChatView
import kotlinx.android.synthetic.main.view_chatting_notice_image_layout.view.*

/**
 * 공지 이미지용 Item ViewHolder
 * Created by  pluu on 2017-05-02.
 */
class DaumNoticeImageViewHolder(v: View) : BaseChattingViewHolder(v) {

    override fun bind(context: Context, item: ChatView?) {
        if (item == null) return

        if (item.hRatio != 0f) {
            itemView.noticeImageView.sethRatio(item.hRatio)
            Glide.with(context)
                    .load(item.imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(itemView.noticeImageView)
        } else {
            Glide.with(context)
                    .load(item.imgUrl)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                            val hRatio = resource.height.toFloat() / resource.width
                            item.hRatio = hRatio
                            itemView.noticeImageView.sethRatio(hRatio)
                            itemView.noticeImageView.setImageBitmap(resource)
                        }
                    })
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup) = DaumNoticeImageViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_chatting_notice_image_layout, parent, false))
    }
}
