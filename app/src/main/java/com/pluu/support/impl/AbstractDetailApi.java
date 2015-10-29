package com.pluu.support.impl;

import android.content.Context;

import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;

/**
 * Detail Parse API
 * Created by PLUUSYSTEM-NEW on 2015-10-26.
 */
public abstract class AbstractDetailApi extends NetworkSupportApi {

	public abstract Detail parseDetail(Context context, Episode episode, String url);

	public abstract ShareItem getDetailShare(Episode episode, Detail detail);


}
