package com.pluu.support.nate;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.WebToonInfo;
import com.pluu.webtoon.ui.BaseActivity.NAV_ITEM;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 네이트 웹툰 Week API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class NateWeekApi extends AbstractWeekApi {

	private static final String[] TITLE = new String[]{"월", "화", "수", "목", "금", "토", "일"};
	private final String WEEKLY_URL = "http://m.comics.nate.com/main/index";

	public NateWeekApi() {
		super(TITLE);
	}

	@Override
	public NAV_ITEM getNaviItem() {
		return NAV_ITEM.NATE;
	}

	@Override
	public int getMainTitleColor(Context context) {
		return R.color.nate_color;
	}

	@Override
	public int getTodayTabPosition() {
		return Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK);
	}

	@Override
	public List<WebToonInfo> parseMain(Context context, String url, int position) {
		ArrayList<WebToonInfo> list = new ArrayList<>();

		String response;
		try {
			response = requestApi();
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}

		Document doc = Jsoup.parse(response);
		Elements links = doc.select(".wkTypeAll_" + position);
		WebToonInfo item;
		Pattern pattern = Pattern.compile("(?<=btno=)\\d+");
		String href;
		for (Element a : links) {
			href = a.absUrl("href");
			Matcher matcher = pattern.matcher(href);
			if (!matcher.find()) {
				continue;
			}

			item = new WebToonInfo(matcher.group());
			item.setUrl(href);
			item.setTitle(a.select(".wtl_title").text());
			item.setImage(a.select(".wtl_img img").first().attr("src"));
			item.setWriter(a.select(".wtl_author").text());
			list.add(item);
		}

		return list;
	}

	@Override
	public String getMethod() {
		return GET;
	}

	@Override
	public String getUrl() {
		return WEEKLY_URL;
	}
}
