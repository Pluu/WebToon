package com.pluu.support.nate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pluu.support.impl.AbstractEpisodeApi;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.EpisodePage;
import com.pluu.webtoon.item.WebToonInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 네이트 웹툰 Episode API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class NateEpisodeApi extends AbstractEpisodeApi {

	private final String EPISODE_URL = "http://m.comics.nate.com/main/info?btno=%s&order=up";
	private final String MORE_EPISODE_URL = "http://m.comics.nate.com/rest/infoList?btno=%s&page=%d";

	private final Pattern EPISODE_ID_PATTERN = Pattern.compile("(?<=bsno=)\\d+");

	private String url;

	private Episode firstEpisode;
	private int pageNo;

	@Override
	public EpisodePage parseEpisode(WebToonInfo info) {
		boolean isMorePage = pageNo > 1;

		if (isMorePage) {
			this.url = String.format(MORE_EPISODE_URL, info.getToonId(), pageNo);
		} else {
			this.url = String.format(EPISODE_URL, info.getToonId());
		}

		EpisodePage episodePage = new EpisodePage(this);

		String response;
		try {
			response = requestApi();
		} catch (Exception e) {
			e.printStackTrace();
			return episodePage;
		}

		Document doc = Jsoup.parse(response);

		try {
			if (isMorePage) {
				episodePage.episodes = parseList(info, new JSONArray(response));
			} else {
				if (firstEpisode == null) {
					String href = doc.select(".btn_1stEpisode").first().attr("href");
					Matcher matcher = EPISODE_ID_PATTERN.matcher(href);
					if (matcher.find()) {
						firstEpisode = new Episode(info, matcher.group());
					}
				}

				episodePage.episodes = parseList(info, doc);
			}

			if (!episodePage.episodes.isEmpty()) {
				episodePage.nextLink = info.getToonId();
			}

			pageNo++;
		} catch (JSONException e) {
			e.printStackTrace();
			episodePage.episodes = new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return episodePage;
	}

	private List<Episode> parseList(WebToonInfo info, Document doc) {
		List<Episode> list = new ArrayList<>();
		Elements links = doc.select(".first");
		Episode item;

		try {
			Matcher matcher;
			String href;

			for (Element a : links) {
				href = a.select("a").first().attr("href");

				matcher = EPISODE_ID_PATTERN.matcher(href);
				if (!matcher.find()) {
					continue;
				}

				item = new Episode(info, matcher.group());
				item.setImage(a.select("img").first().attr("src"));
				item.setEpisodeTitle(a.select(".tel_episode").text() + " " + a.select(".tel_title").text());
				item.setUpdateDate(a.select(".tel_date").text());
				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	private List<Episode> parseList(WebToonInfo info, JSONArray array) {
		List<Episode> list = new ArrayList<>();
		Episode item;

		JSONObject obj;
		for (int i = 0; i < array.length(); i++) {
			obj = array.optJSONObject(i);
			item = new Episode(info, obj.optString("bsno"));
			item.setImage(obj.optString("thumb_app"));
			item.setEpisodeTitle(obj.optString("volume_txt") + " " + obj.optString("sub_title"));
			item.setUpdateDate(obj.optString("date_write"));
			list.add(item);
		}

		return list;
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
		pageNo = 1;
	}

	@Override
	public String getMethod() {
		if (pageNo > 1) {
			return POST;
		} else {
			return GET;
		}
	}

	@Override
	public String getUrl() {
		return url;
	}

}
