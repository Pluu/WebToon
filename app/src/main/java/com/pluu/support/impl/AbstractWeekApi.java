package com.pluu.support.impl;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.pluu.support.daum.DaumWeekApi;
import com.pluu.support.impl.ServiceConst.NAV_ITEM;
import com.pluu.support.kakao.KakaoWeekApi;
import com.pluu.support.nate.NateWeekApi;
import com.pluu.support.naver.NaverWeekApi;
import com.pluu.support.olleh.OllehWeekApi;
import com.pluu.support.tstore.TStorerWeekApi;
import com.pluu.webtoon.item.WebToonInfo;

/**
 * Week API
 * Created by PLUUSYSTEM-NEW on 2015-10-26.
 */
public abstract class AbstractWeekApi extends NetworkSupportApi {

	private final String[] CURRENT_TABS;

	public AbstractWeekApi(String[] tabs) {
		this.CURRENT_TABS = tabs;
	}

	public abstract NAV_ITEM getNaviItem();

	public int getTitleColor(Context context) {
		return ContextCompat.getColor(context, getMainTitleColor());
	}

	protected abstract int getMainTitleColor();

	public int getWeeklyTabSize() {
		return CURRENT_TABS.length;
	}

	public String getWeeklyTabName(int position) {
		return CURRENT_TABS[position];
	}

	public int getTodayTabPosition() {
		return (Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK) + 5) % 7;
	}

	public abstract List<WebToonInfo> parseMain(int position);

	public static AbstractWeekApi getApi(NAV_ITEM item) {
		switch (item) {
			case NAVER:
				return new NaverWeekApi();
			case DAUM:
				return new DaumWeekApi();
			case OLLEH:
				return new OllehWeekApi();
			case KAKAOPAGE:
				return new KakaoWeekApi();
			case NATE:
				return new NateWeekApi();
			case T_STORE:
				return new TStorerWeekApi();
			default:
				throw new Resources.NotFoundException("Not Found API");
		}
	}

}
