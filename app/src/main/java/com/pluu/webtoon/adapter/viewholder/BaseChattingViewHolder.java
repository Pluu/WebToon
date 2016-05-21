package com.pluu.webtoon.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pluu.webtoon.R;
import com.pluu.webtoon.item.ChatView;

/**
 * Created by PLUUSYSTEM-SURFACE on 2016-05-21.
 */
public abstract class BaseChattingViewHolder extends RecyclerView.ViewHolder {

    public BaseChattingViewHolder(View v) {
        super(v);
    }

    protected void loadProfileImage(Context context, ImageView view, String url) {
        final int profileSize = context.getResources().getDimensionPixelSize(R.dimen.chatting_profile_size);

        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(profileSize, profileSize)
                .centerCrop()
                .placeholder(R.drawable.transparent_background)
                .into(view);
    }

    public abstract void bind(Context context, ChatView item);

}
