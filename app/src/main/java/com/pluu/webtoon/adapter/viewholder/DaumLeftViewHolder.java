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
 * 좌측 표시용 Item ViewHolder
 * Created by PLUUSYSTEM-SURFACE on 2016-05-21.
 */
public class DaumLeftViewHolder extends BaseChattingViewHolder {

    @BindView(R.id.leftProfileImageView)
    ImageView leftProfileImageView;
    @BindView(R.id.leftNameTextView)
    TextView leftNameTextView;
    @BindView(R.id.leftMessageTextView)
    TextView leftMessageTextView;

    public DaumLeftViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    @Override
    public void bind(Context context, ChatView item) {
        loadProfileImage(context, leftProfileImageView, item.getImgUrl());
        leftNameTextView.setText(item.getName());
        leftMessageTextView.setText(item.getText());
    }
}
