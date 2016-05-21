package com.pluu.webtoon.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pluu.webtoon.R;
import com.pluu.webtoon.item.ChatView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by PLUUSYSTEM-SURFACE on 2016-05-21.
 */

public class DaumRIghtViewHolder extends BaseChattingViewHolder {

    @Bind(R.id.rightProfileImageView)
    ImageView rightProfileImageView;
    @Bind(R.id.rightNameTextView)
    TextView rightNameTextView;
    @Bind(R.id.rightMessageTextView)
    TextView rightMessageTextView;

    public DaumRIghtViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    @Override
    public void bind(Context context, ChatView item) {
        loadProfileImage(context, rightProfileImageView, item.getImgUrl());
        rightNameTextView.setText(item.getName());
        rightMessageTextView.setText(item.getText());
    }
}
