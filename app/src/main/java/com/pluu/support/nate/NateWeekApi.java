package com.pluu.support.nate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.R;
import com.pluu.webtoon.item.WebToonInfo;
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
	public ServiceConst.NAV_ITEM getNaviItem() {
		return ServiceConst.NAV_ITEM.NATE;
	}

	@Override
	protected int getMainTitleColor() {
		return R.color.nate_color;
	}

	@Override
	public List<WebToonInfo> parseMain(int position) {
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
			href = a.attr("href");
			Matcher matcher = pattern.matcher(href);
			if (!matcher.find()) {
				continue;
			}

			item = new WebToonInfo(matcher.group());
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
