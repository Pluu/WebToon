package com.pluu.webtoon.event;

import android.support.annotation.ColorInt;

/**
 * Theme Change Event
 * Created by PLUUSYSTEM-NEW on 2015-11-22.
 */
public class ThemeEvent {

	private final int color, darlColor;

	public ThemeEvent(@ColorInt int color, @ColorInt int darlColor) {
		this.color = color;
		this.darlColor = darlColor;
	}

	public int getColor() {
		return color;
	}

	public int getDarlColor() {
		return darlColor;
	}
}
