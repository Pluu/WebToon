package com.pluu.webtoon.adapter.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pluu.webtoon.R;
import com.pluu.webtoon.item.ChatView;
import com.pluu.webtoon.ui.view.AspectRatioImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 공지 이미지용 Item ViewHolder
 * Created by PLUUSYSTEM-SURFACE on 2016-05-21.
 */
public class DaumNoticeImageViewHolder extends BaseChattingViewHolder {
    @BindView(R.id.noticeImageView)
    AspectRatioImageView noticeImageView;

    public DaumNoticeImageViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    @Override
    public void bind(Context context, final ChatView item) {
        if (item.getHRatio() != 0) {
            noticeImageView.sethRatio(item.getHRatio());
            Glide.with(context)
                    .load(item.getImgUrl())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(noticeImageView);
        } else {
            Glide.with(context)
                    .load(item.getImgUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            float hRatio = (float) resource.getHeight() / resource.getWidth();
                            item.setHRatio(hRatio);
                            noticeImageView.sethRatio(hRatio);
                            noticeImageView.setImageBitmap(resource);
                        }
                    });
        }
    }
}
