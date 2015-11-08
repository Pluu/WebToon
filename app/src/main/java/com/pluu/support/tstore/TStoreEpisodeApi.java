package com.pluu.support.tstore;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pluu.support.impl.AbstractEpisodeApi;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.EpisodePage;
import com.pluu.webtoon.item.WebToonInfo;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * TStore 웹툰 에피소드 Api
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class TStoreEpisodeApi extends AbstractEpisodeApi {

	private final String EPISODE_URL = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonList.omp?prodId=%s";
	private final String MORE_EPISODE_URL = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonListMore.omp";

	private final Pattern URL_PATTERN = Pattern.compile("(?<=goInnerUrlDetail\\(\\\\\\').+(?=\\\\'\\)'\\);)");
	private final Pattern EPISODE_ID = Pattern.compile("(?<=prodId=)\\w+");

	private String id;
	private int pageNo;
	private Episode firstEpisode;

	@Override
	public EpisodePage parseEpisode(WebToonInfo info) {
		this.id = info.getWebtoonId();

		EpisodePage episodePage = new EpisodePage(this);

		try {
			if (pageNo > 0) {
				JSONObject subJson = getMoreJson(info.getWebtoonId(), pageNo);
				episodePage.episodes = parseList(info, subJson.optJSONArray("webtoonList"));
				pageNo++;
			} else {
				String response = requestApi();
				Document doc = Jsoup.parse(response);
				firstEpisode = getFirstItem(info, doc);
				episodePage.episodes = parseList(info, doc);
				pageNo = 3;
			}

			if (!episodePage.episodes.isEmpty()) {
				episodePage.nextLink = info.getWebtoonId();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return episodePage;
		}

		return episodePage;
	}

	private JSONObject getMoreJson(String id, int pageNo) throws Exception {
		Request.Builder builder = new Request.Builder()
			.url(MORE_EPISODE_URL);

		FormEncodingBuilder fromBuilder = new FormEncodingBuilder();
		fromBuilder.add("prodId", id);
		fromBuilder.add("currentPage", String.valueOf(pageNo));
		RequestBody requestBody = fromBuilder.build();
		builder.post(requestBody);

		String response = requestApi(builder.build());
		return new JSONObject(response);
	}

	private List<Episode> parseList(WebToonInfo info, JSONArray array) {
		int size = array.length();
		List<Episode> list = new ArrayList<>(size);

		JSONObject obj;
		Episode item;
		for (int i = 0; i < size; i++) {
			obj = array.optJSONObject(i);

			item = new Episode(info, obj.optString("prodId"));
			item.setImage(obj.optString("filePos"));
			item.setEpisodeTitle(obj.optString("prodNm"));
			item.setUpdateDate(obj.optString("updateDate"));

			list.add(item);
		}
		return list;
	}

	private List<Episode> parseList(WebToonInfo info, Document doc) {
		List<Episode> list = new ArrayList<>();
		Elements links = doc.select("ul[class=list-one type-comic2] a");
		Episode item;

		try {
			Matcher matcher, matcher2;
			String href;

			for (Element a : links) {
				href = a.attr("href");

				matcher = URL_PATTERN.matcher(href);
				if (!matcher.find()) {
					continue;
				}
				matcher2 = EPISODE_ID.matcher(matcher.group());
				if (!matcher2.find()) {
					continue;
				}

				item = new Episode(info, matcher2.group());
				item.setImage(a.select(".thum img").last().attr("src"));
				item.setEpisodeTitle(a.select(".detail dt").text());
				item.setUpdateDate(a.select(".txt").text());
				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	private Episode getFirstItem(WebToonInfo info, Document doc) {
		String href = doc.select(".toon-btn-area .btn-wht").attr("href");

		Matcher matcher, matcher2;
		matcher = URL_PATTERN.matcher(href);
		if (!matcher.find()) {
			return null;
		}
		matcher2 = EPISODE_ID.matcher(matcher.group());
		if (!matcher2.find()) {
			return null;
		}

		Episode ret = new Episode(info, matcher2.group());
		return ret;
	}

	@Override
	public String moreParseEpisode(EpisodePage item) {
		return item.nextLink;
	}

	@Override
	public Episode getFirstEpisode(Episode item) {
		return firstEpisode;
	}

	@Override
	public void init() {
		super.init();
		pageNo = 0;
	}

	@Override
	public String getMethod() {
		return GET;
	}

	@Override
	public String getUrl() {
		return String.format(EPISODE_URL, id);
	}

}
