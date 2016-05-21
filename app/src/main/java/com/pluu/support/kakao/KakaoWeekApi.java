package com.pluu.support.kakao;

import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
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

/**
 * 카카오 페이지 웹툰 Week API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class KakaoWeekApi extends AbstractWeekApi {

	private static final String[] TITLE = new String[]{"월", "화", "수", "목", "금", "토", "일"};
	private final String WEEK_URL = "http://page.kakao.com/main/ajaxCallWeeklyList";
	private int currentPos;

	public KakaoWeekApi() {
		super(TITLE);
	}

	@Override
	public ServiceConst.NAV_ITEM getNaviItem() {
		return ServiceConst.NAV_ITEM.KAKAOPAGE;
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

		final String idAttribute = "data-seriesId";
		for (Element a : links) {
			if (!a.hasAttr(idAttribute)) {
				continue;
			}

			WebToonInfo item = new WebToonInfo(a.attr(idAttribute));
			item.setTitle(a.select(".title").first().text());
			item.setImage(a.select(".thumbnail img").first().attr("src"));

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
