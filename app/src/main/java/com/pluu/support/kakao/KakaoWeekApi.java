package com.pluu.support.kakao;

import android.content.Context;

import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.NAV_ITEM;
import com.pluu.webtoon.item.Status;
import com.pluu.webtoon.item.WebToonInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 카카오 페이지 웹툰 Week API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class KakaoWeekApi extends AbstractWeekApi {

	private static final String[] TITLE = new String[]{"월", "화", "수", "목", "금", "토", "일"};
	private final String WEEK_URL = "http://page.kakao.com/main/ajaxCallWeeklyList";
	private int currentPos;

	public KakaoWeekApi(Context context) {
		super(context, TITLE);
	}

	@Override
	public NAV_ITEM getNaviItem() {
		return NAV_ITEM.KAKAOPAGE;
	}

	@Override
	public List<WebToonInfo> parseMain(int position) {
		this.currentPos = position;

		ArrayList<WebToonInfo> list = new ArrayList<>();

		String response;
		try {
			response = requestApi();
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}

		Document doc = Jsoup.parse(response);
		Elements links = doc.select(".list");

		final Pattern pattern = Pattern.compile("(?<=/home/)\\d+(?=\\?categoryUid)");
		for (Element a : links) {
            String href = a.select("a").attr("href");
            Matcher matcher = pattern.matcher(href);
            if (!matcher.find()) {
                continue;
            }
            WebToonInfo item = new WebToonInfo(matcher.group());
			item.setTitle(a.select(".title").first().text());
			item.setImage(a.select(".thumbnail img").last().attr("src"));

			if (!a.select(".badgeImg").isEmpty()) {
				item.setStatus(Status.UPDATE);
			}
			item.setWriter(a.select(".info ").text().split("•")[1]);
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
		return WEEK_URL;
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> map = new HashMap<>();
		map.put("navi", "1");
		map.put("day", String.valueOf(currentPos + 1));
		map.put("inkatalk", "0");
		map.put("categoryUid", "10");
		map.put("subCategoryUid", "1000");
		return map;
	}
}
