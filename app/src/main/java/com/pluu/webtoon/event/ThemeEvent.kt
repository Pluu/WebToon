package com.pluu.webtoon.event

import androidx.annotation.ColorInt
import java.io.Serializable

/**
 * Theme Change Event
 * Created by pluu on 2017-04-18.
 */
class ThemeEvent(
    @ColorInt val color: Int,
    @ColorInt val variantColor: Int
) : Serializable
