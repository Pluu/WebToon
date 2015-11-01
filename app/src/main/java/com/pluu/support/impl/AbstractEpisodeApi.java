package com.pluu.support.impl;

import android.content.Context;
import android.content.res.Resources;

import com.pluu.support.daum.DaumEpisodeApi;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.support.kakao.KakaoEpisodeApi;
import com.pluu.support.nate.NateEpisodeApi;
import com.pluu.support.naver.NaverEpisodeApi;
import com.pluu.support.olleh.OllehEpisodeApi;
import com.pluu.support.tstore.TStoreEpisodeApi;
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

	public static AbstractEpisodeApi getApi(NAV_ITEM item) {
		switch (item) {
			case NAVER:
				return new NaverEpisodeApi();
			case DAUM:
				return new DaumEpisodeApi();
			case OLLEH:
				return new OllehEpisodeApi();
			case KAKAOPAGE:
				return new KakaoEpisodeApi();
			case NATE:
				return new NateEpisodeApi();
			case T_STORE:
				return new TStoreEpisodeApi();
			default:
				throw new Resources.NotFoundException("Not Found API");
		}
	}

}
