package com.pluu.webtoon.event;

/**
 * Theme Change Event
 * Created by PLUUSYSTEM-NEW on 2015-11-22.
 */
public class ThemeEvent {

	private final int color, darlColor;

	public ThemeEvent(int color, int darlColor) {
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
