package com.pluu.support.kakao;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.Status;
import com.pluu.webtoon.api.WebToonInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 카카오 페이지 웹툰 Week API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class KakaoWeekApi extends AbstractWeekApi {

	private static final String[] TITLE = new String[]{"월", "화", "수", "목", "금", "토", "일"};
	private final String WEEK_URL = "http://page.kakao.com/main/ajax_weekly_web";
	private int currentPos;

	public KakaoWeekApi() {
		super(TITLE);
	}

	@Override
	public ServiceConst.NAV_ITEM getNaviItem() {
		return ServiceConst.NAV_ITEM.KAKAOPAGE;
	}

	@Override
	protected int getMainTitleColor(Context context) {
		return R.color.kakao_color;
	}

	@Override
	public List<WebToonInfo> parseMain(Context context, int position) {
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
		Elements links = doc.select(".l_link");
		WebToonInfo item = null;
		Pattern pattern = Pattern.compile("(?<=/home/)\\d+");
		String href;

		String updateStatus = "up";
		for (Element a : links) {
			href = a.attr("data-href");
			Matcher matcher = pattern.matcher(href);
			if (!matcher.find()) {
				continue;
			}

			item = new WebToonInfo(matcher.group());
			item.setUrl(href);
			item.setTitle(a.select(".title").first().text());
			item.setImage(a.select(".listImg").first().attr("src"));

			if (updateStatus.equals(a.select(".badgeImg").attr("alt"))) {
				item.setStatus(Status.UPDATE);
			}
			item.setWriter(a.select("span[class=info elInfo ellipsis]").text());
			list.add(item);
		}

		return list;
	}

	@Override
	public String getMethod() {
		return GET;
	}

	@Override
	public String getId() {
		return WEEK_URL;
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> map = new HashMap<>();
		map.put("week", String.valueOf(currentPos + 1));
		map.put("categoryType", "MC09");
		map.put("navi", "1");
		map.put("inkatalk", "0");
		map.put("categoryUid", "10");
		map.put("subCategoryUid", "0");
		return map;
	}
}
