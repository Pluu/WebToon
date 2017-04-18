/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.iosched.ui.widget

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout

internal class SlidingTabStrip @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private val mBottomBorderThickness: Int
    private val mBottomBorderPaint: Paint

    private val mSelectedIndicatorThickness: Int
    private val mSelectedIndicatorPaint: Paint

    private val mDefaultBottomBorderColor: Int

    private var mSelectedPosition: Int = 0
    private var mSelectionOffset: Float = 0.toFloat()

    private var mCustomTabColorizer: SlidingTabLayout.TabColorizer? = null
    private val mDefaultTabColorizer: SimpleTabColorizer

    init {
        setWillNotDraw(false)

        val density = resources.displayMetrics.density

        val outValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorForeground, outValue, true)
        val themeForegroundColor = outValue.data

        mDefaultBottomBorderColor = setColorAlpha(themeForegroundColor,
                DEFAULT_BOTTOM_BORDER_COLOR_ALPHA)

        mDefaultTabColorizer = SimpleTabColorizer().apply {
            setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR)
        }

        mBottomBorderThickness = (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density).toInt()
        mBottomBorderPaint = Paint().apply {
            color = mDefaultBottomBorderColor
        }

        mSelectedIndicatorThickness = (SELECTED_INDICATOR_THICKNESS_DIPS * density).toInt()
        mSelectedIndicatorPaint = Paint()
    }

    fun setCustomTabColorizer(customTabColorizer: SlidingTabLayout.TabColorizer) {
        mCustomTabColorizer = customTabColorizer
        invalidate()
    }

    fun setSelectedIndicatorColors(vararg colors: Int) {
        // Make sure that the custom colorizer is removed
        mCustomTabColorizer = null
        mDefaultTabColorizer.setIndicatorColors(*colors)
        invalidate()
    }

    fun onViewPagerPageChanged(position: Int, positionOffset: Float) {
        mSelectedPosition = position
        mSelectionOffset = positionOffset
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val height = height
        val childCount = childCount
        val tabColorizer = mCustomTabColorizer ?: mDefaultTabColorizer

        // Thick colored underline below the current selection
        if (childCount > 0) {
            val selectedTitle = getChildAt(mSelectedPosition)
            var left = selectedTitle.left
            var right = selectedTitle.right
            var color = tabColorizer.getIndicatorColor(mSelectedPosition)

            if (mSelectionOffset > 0f && mSelectedPosition < getChildCount() - 1) {
                val nextColor = tabColorizer.getIndicatorColor(mSelectedPosition + 1)
                if (color != nextColor) {
                    color = blendColors(nextColor, color, mSelectionOffset)
                }

                // Draw the selection partway between the tabs
                val nextTitle = getChildAt(mSelectedPosition + 1)
                left = (mSelectionOffset * nextTitle.left + (1.0f - mSelectionOffset) * left).toInt()
                right = (mSelectionOffset * nextTitle.right + (1.0f - mSelectionOffset) * right).toInt()
            }

            mSelectedIndicatorPaint.color = color

            canvas.drawRect(left.toFloat(), (height - mSelectedIndicatorThickness).toFloat(), right.toFloat(),
                    height.toFloat(), mSelectedIndicatorPaint)
        }

        // Thin underline along the entire bottom edge
        canvas.drawRect(0f, (height - mBottomBorderThickness).toFloat(), width.toFloat(), height.toFloat(), mBottomBorderPaint)
    }

    private class SimpleTabColorizer : SlidingTabLayout.TabColorizer {
        private var mIndicatorColors: IntArray? = null

        override fun getIndicatorColor(position: Int): Int {
            return mIndicatorColors!![position % mIndicatorColors!!.size]
        }

        internal fun setIndicatorColors(vararg colors: Int) {
            mIndicatorColors = colors
        }
    }

    companion object {

        private val DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 0
        private val DEFAULT_BOTTOM_BORDER_COLOR_ALPHA: Byte = 0x26
        private val SELECTED_INDICATOR_THICKNESS_DIPS = 3
        private val DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF33B5E5.toInt()

        /**
         * Set the alpha value of the `color` to be the given `alpha` value.
         */
        private fun setColorAlpha(color: Int, alpha: Byte): Int {
            return Color.argb(alpha.toInt(), Color.red(color), Color.green(color), Color.blue(color))
        }

        /**
         * Blend `color1` and `color2` using the given ratio.

         * @param ratio of which to blend. 1.0 will return `color1`, 0.5 will give an even blend,
         * *              0.0 will return `color2`.
         */
        private fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
            val inverseRation = 1f - ratio
            val r = Color.red(color1) * ratio + Color.red(color2) * inverseRation
            val g = Color.green(color1) * ratio + Color.green(color2) * inverseRation
            val b = Color.blue(color1) * ratio + Color.blue(color2) * inverseRation
            return Color.rgb(r.toInt(), g.toInt(), b.toInt())
        }
    }
}