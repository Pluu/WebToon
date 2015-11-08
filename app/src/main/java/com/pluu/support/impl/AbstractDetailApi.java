package com.pluu.support.impl;

import android.content.res.Resources;

import com.pluu.support.daum.DaumDetailApi;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.support.kakao.KakaoDetailApi;
import com.pluu.support.nate.NateDetailApi;
import com.pluu.support.naver.NaverDetailApi;
import com.pluu.support.olleh.OllehDetailApi;
import com.pluu.support.tstore.TStoreDetailApi;
import com.pluu.webtoon.item.Detail;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.ShareItem;

/**
 * Detail Parse API
 * Created by PLUUSYSTEM-NEW on 2015-10-26.
 */
public abstract class AbstractDetailApi extends NetworkSupportApi {

	public abstract Detail parseDetail(Episode episode);

	public abstract ShareItem getDetailShare(Episode episode, Detail detail);

	public static AbstractDetailApi getApi(NAV_ITEM item) {
		switch (item) {
			case NAVER:
				return new NaverDetailApi();
			case DAUM:
				return new DaumDetailApi();
			case OLLEH:
				return new OllehDetailApi();
			case KAKAOPAGE:
				return new KakaoDetailApi();
			case NATE:
				return new NateDetailApi();
			case T_STORE:
				return new TStoreDetailApi();
			default:
				throw new Resources.NotFoundException("Not Found API");
		}
	}

}
