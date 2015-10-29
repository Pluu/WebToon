package com.pluu.support.impl;

import android.content.Context;

import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;

/**
 * Episode API
 * Created by PLUUSYSTEM-NEW on 2015-10-26.
 */
public abstract class AbstractEpisodeApi extends NetworkSupportApi {

	public void init() { }

	public abstract WebToon parseEpisode(Context context, WebToonInfo info, String url);

	public abstract String moreParseEpisode(WebToon item);

	public abstract Episode getFirstEpisode(Episode item);



}
