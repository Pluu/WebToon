package com.pluu.webtoon.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Width Fix, Height Ratio ImageView
 * Created by PLUUSYSTEM-NEW on 2016-01-28.
 */
public class AspectRatioImageView extends ImageView {

    private float hRatio = -1;

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (hRatio == -1) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * hRatio);
            setMeasuredDimension(width, height);
        }
    }

    public void sethRatio(float hRatio) {
        this.hRatio = hRatio;
    }
}
