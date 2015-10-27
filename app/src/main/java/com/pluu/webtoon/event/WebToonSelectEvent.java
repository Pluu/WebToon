package com.pluu.webtoon.event;

import com.pluu.webtoon.api.WebToonInfo;

/**
 * Episode Select Event
 * Created by PLUUSYSTEM-NEW on 2015-10-27.
 */
public class WebToonSelectEvent {

	public final WebToonInfo item;

	public WebToonSelectEvent(WebToonInfo item) {
		this.item = item;
	}
}
