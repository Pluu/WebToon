package com.pluu.support.tstore;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pluu.support.impl.AbstractWeekApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.R;
import com.pluu.webtoon.item.Status;
import com.pluu.webtoon.item.WebToonInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * TStore 웹툰 Week Api
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class TStorerWeekApi extends AbstractWeekApi {

	private static final String[] TITLE = new String[]{"월", "화", "수", "목", "금", "토", "T툰"};
	private final String WEEKLY_URL = "http://m.tstore.co.kr/mobilepoc/webtoon/weekdayList.omp?weekday=";
	private final Pattern URL_PATTERN = Pattern.compile("(?<=goInnerUrlDetail\\(\\\\\\').+(?=\\\\'\\)'\\);)");
	private final Pattern ID_PATTERN = Pattern.compile("(?<=prodId=).+(?=&)");

	private int currentPos;

	public TStorerWeekApi() {
		super(TITLE);
	}

	@Override
	public ServiceConst.NAV_ITEM getNaviItem() {
		return ServiceConst.NAV_ITEM.T_STORE;
	}

	@Override
	public List<WebToonInfo> parseMain(int position) {
		this.currentPos = position;

		ArrayList<WebToonInfo> list = new ArrayList<>();

		try {
			String response = requestApi();
			Document doc = Jsoup.parse(response);

			WebToonInfo item;
			String href;

			Matcher matcher, matcher2;
			Elements bottom = doc.select("#weekToon0" + (position + 1) + " ul li a");

			for (Element a : bottom) {
				href = a.attr("href");
				matcher = URL_PATTERN.matcher(href);
				if (!matcher.find()) {
					continue;
				}
				matcher2 = ID_PATTERN.matcher(matcher.group());
				if (!matcher2.find()) {
					continue;
				}

				item = new WebToonInfo(matcher2.group());
				item.setTitle(a.select(".detail dl dt").text());
				item.setImage(a.select(".thum img").last().attr("src"));
				item.setWriter(a.select(".txt").text());
//				item.setRate(a.select(".cRed1").text());
				item.setUpdateDate(a.select(".grade strong").text());

				if (!a.select(".new-up").isEmpty()) {
					// 최근 업데이트
					item.setStatus(Status.UPDATE);
				}

				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public String getMethod() {
		return POST;
	}

	@Override
	public String getUrl() {
		return WEEKLY_URL + (currentPos + 1) ;
	}
}
