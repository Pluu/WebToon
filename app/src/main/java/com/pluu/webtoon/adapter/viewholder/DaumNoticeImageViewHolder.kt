package com.pluu.webtoon.adapter.viewholder

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.pluu.kotlin.inflate
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

        val option = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
        if (item.hRatio != 0f) {
            itemView.noticeImageView.sethRatio(item.hRatio)
            Glide.with(context)
                .load(item.imgUrl)
                .apply(option)
                .into(itemView.noticeImageView)
        } else {
            Glide.with(context)
                .asBitmap()
                .load(item.imgUrl)
                .apply(option)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        item.hRatio = resource.height.toFloat() / resource.width
                        itemView.noticeImageView.sethRatio(item.hRatio)
                        itemView.noticeImageView.setImageBitmap(resource)
                    }
                })
        }
    }

    companion object {
        fun newInstance(parent: ViewGroup) = DaumNoticeImageViewHolder(
            parent.inflate(R.layout.view_chatting_notice_image_layout, false)
        )
    }
}
