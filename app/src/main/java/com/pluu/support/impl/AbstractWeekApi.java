package com.pluu.support.impl;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.pluu.webtoon.api.WebToonInfo;
import com.pluu.webtoon.ui.BaseActivity.NAV_ITEM;

;

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

	// TODO : 컬러 취득 로직 추후 수정 필요
	public int getTitleColor(Context context) {
		return ContextCompat.getColor(context, getMainTitleColor(context));
	}

	public abstract int getMainTitleColor(Context context);

	public int getWeeklyTabSize() {
		return CURRENT_TABS.length;
	}

	public String getWeeklyTabName(int position) {
		return CURRENT_TABS[position];
	}

	protected int getTodayPosition() {
		return Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK);
	}

	public abstract int getTodayTabPosition();

	public abstract List<WebToonInfo> parseMain(Context context, String url, int position);

}
