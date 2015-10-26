package com.pluu.support;

import android.content.Context;

import java.util.Calendar;
import java.util.Locale;

import com.pluu.webtoon.R;
import com.pluu.webtoon.api.WebToon;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * Created by nohhs on 2015-03-12.
 */
public abstract class BaseApiImpl implements ApiImpl {

	private final String[] CURRENT_TABS;

	public BaseApiImpl(String[] tabs) {
		this.CURRENT_TABS = tabs;
	}

	protected Connection getConnection(String url) {
		return Jsoup.connect(url).timeout(3000);
	}

	@Override
	public int getMainTitleColor(Context context) {
		return context.getResources().getColor(R.color.theme_primary);
	}

	@Override
	public int getWeeklyTabSize() {
		return CURRENT_TABS.length;
	}

	@Override
	public String getWeeklyTabName(int position) {
		return CURRENT_TABS[position];
	}

	protected int getTodayPosition() {
		return Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK);
	}

	@Override
	public int getTodayTabPosition() {
		return (getTodayPosition() + 5) % 7;
	}

	@Override
	public String moreParseEpisode(WebToon item) {
		return item.nextLink;
	}

	@Override
	public void refreshEpisode() { }

	@Override
	public boolean isLoginDataExist(Context context) {
		return false;
	}

}
