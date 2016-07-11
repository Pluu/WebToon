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
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.EpisodePage;
import com.pluu.webtoon.item.WebToonInfo;

/**
 * Episode API
 * Created by PLUUSYSTEM-NEW on 2015-10-26.
 */
public abstract class AbstractEpisodeApi extends NetworkSupportApi {

	public AbstractEpisodeApi(Context context) {
		super(context);
	}

	public void init() { }

	public abstract EpisodePage parseEpisode(WebToonInfo info);

	public abstract String moreParseEpisode(EpisodePage item);

	public abstract Episode getFirstEpisode(Episode item);

	public static AbstractEpisodeApi getApi(Context context, NAV_ITEM item) {
		switch (item) {
			case NAVER:
				return new NaverEpisodeApi(context);
			case DAUM:
				return new DaumEpisodeApi(context);
			case OLLEH:
				return new OllehEpisodeApi(context);
			case KAKAOPAGE:
				return new KakaoEpisodeApi(context);
			case NATE:
				return new NateEpisodeApi(context);
			case T_STORE:
				return new TStoreEpisodeApi(context);
			default:
				throw new Resources.NotFoundException("Not Found API");
		}
	}

}
