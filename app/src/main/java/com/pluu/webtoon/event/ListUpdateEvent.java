package com.pluu.webtoon.event;

import com.pluu.webtoon.item.WebToonInfo;

/**
 * Main List Episode Update Event
 * Created by PLUUSYSTEM-NEW on 2015-11-06.
 */
public class ListUpdateEvent {

	public final WebToonInfo info;

	public ListUpdateEvent(WebToonInfo info) {
		this.info = info;
	}
}
