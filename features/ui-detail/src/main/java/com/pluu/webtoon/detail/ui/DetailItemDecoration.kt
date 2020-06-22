package com.pluu.webtoon.detail.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class DetailItemDecoration(
    private val top: Int,
    private val bottom: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val count: Int = parent.adapter?.itemCount ?: return

        when (parent.getChildAdapterPosition(view)) {
            0 -> outRect.set(0, top, 0, 0)
            count - 1 -> outRect.set(0, 0, 0, bottom)
        }
    }
}
