package com.pluu.support;

import android.content.Context;

import java.util.List;

import com.pluu.webtoon.ui.BaseActivity.NAV_ITEM;
import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;

/**
 * Base API Interface
 * Created by nohhs on 2015-03-12.
 */
public interface ApiImpl {

	// NavigationDrawer 전용
	NAV_ITEM getNaviItem();

	// Service Color Theme 전용
	int getMainTitleColor(Context context);

	// Service Data

	String getWeeklyUrl(int position);

	int getWeeklyTabSize();

	String getWeeklyTabName(int position);

	int getTodayTabPosition();

	List<WebToonInfo> parseMain(Context context, String url, int position);

	WebToon parseEpisode(Context context, WebToonInfo info, String url);

	String moreParseEpisode(WebToon item);

	Detail parseDetail(Context context, Episode episode, String url);

	Episode getFirstEpisode(Episode item);

	ShareItem getDetailShare(Episode episode, Detail detail);

	void refreshEpisode();

	/**
	 * 로그인 데이터 존재여부 체크
	 * @param context Context
	 * @return true/false
	 */
	boolean isLoginDataExist(Context context);

}
