package com.pluu.webtoon.ui.view

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View

/**
 * Width Fix, Height Ratio ImageView
 * Created by pluu on 2017-05-26.
 */
class AspectRatioImageView : AppCompatImageView {

    private var hRatio = -1f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (hRatio == -1f) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        } else {
            val width = View.MeasureSpec.getSize(widthMeasureSpec)
            val height = (width * hRatio).toInt()
            setMeasuredDimension(width, height)
        }
    }

    fun sethRatio(hRatio: Float) {
        this.hRatio = hRatio
    }
}
