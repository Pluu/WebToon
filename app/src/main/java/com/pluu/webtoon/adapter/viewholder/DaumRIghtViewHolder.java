package com.pluu.webtoon.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pluu.webtoon.R;
import com.pluu.webtoon.item.ChatView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 우측 표시용 Item ViewHolder
 * Created by PLUUSYSTEM-SURFACE on 2016-05-21.
 */
public class DaumRIghtViewHolder extends BaseChattingViewHolder {

    @BindView(R.id.rightProfileImageView)
    ImageView rightProfileImageView;
    @BindView(R.id.rightNameTextView)
    TextView rightNameTextView;
    @BindView(R.id.rightMessageTextView)
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
