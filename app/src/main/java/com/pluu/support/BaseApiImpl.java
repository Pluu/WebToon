package com.pluu.support;

import android.content.Context;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * Created by nohhs on 2015-03-12.
 */
public abstract class BaseApiImpl {

	private final String[] CURRENT_TABS;

	public BaseApiImpl(String[] tabs) {
		this.CURRENT_TABS = tabs;
	}

	protected Connection getConnection(String url) {
		return Jsoup.connect(url).timeout(3000);
	}

	public int getMainTitleColor(Context context) {
		return context.getResources().getColor(R.color.theme_primary);
	}

	public int getWeeklyTabSize() {
		return CURRENT_TABS.length;
	}

	public String getWeeklyTabName(int position) {
		return CURRENT_TABS[position];
	}

	protected int getTodayPosition() {
		return Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK);
	}

	public int getTodayTabPosition() {
		return (getTodayPosition() + 5) % 7;
	}

	public String moreParseEpisode(WebToon item) {
		return item.nextLink;
	}

	public void refreshEpisode() { }

	public boolean isLoginDataExist(Context context) {
		return false;
	}

	public abstract ServiceConst.NAV_ITEM getNaviItem();

	public abstract String getWeeklyUrl(int position);

	public abstract List<WebToonInfo> parseMain(Context context, String url, int position);

	public abstract WebToon parseEpisode(Context context, WebToonInfo info, String url);

	public abstract Episode getFirstEpisode(Episode item);

	public abstract Detail parseDetail(Context context, Episode episode, String url);

	public abstract ShareItem getDetailShare(Episode episode, Detail detail);

}
