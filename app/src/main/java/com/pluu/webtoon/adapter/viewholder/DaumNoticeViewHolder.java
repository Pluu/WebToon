package com.pluu.webtoon.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pluu.webtoon.item.ChatView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by PLUUSYSTEM-SURFACE on 2016-05-21.
 */

public class DaumNoticeViewHolder extends BaseChattingViewHolder {
    @Bind(android.R.id.text1)
    TextView text1;

    public DaumNoticeViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    @Override
    public void bind(Context context, ChatView item) {
        text1.setText(item.getText());
    }
}
