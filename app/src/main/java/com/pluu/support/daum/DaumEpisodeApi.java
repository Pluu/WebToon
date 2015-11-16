package com.pluu.support.daum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pluu.support.impl.AbstractEpisodeApi;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.EpisodePage;
import com.pluu.webtoon.item.WebToonInfo;
import com.squareup.okhttp.Request;

/**
 * 다음 웹툰 Episode API
 * Created by PLUUSYSTEM-NEW on 2015-10-30.
 */
public class DaumEpisodeApi extends AbstractEpisodeApi {

	private static final String EPISODE_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/list_episode_by_nickname";
	private static final String PREFIX_FIRST_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/view?nickname=";

	private String name;

	private int firstEpisodeId;
	private int pageNo = 0;

	@Override
	public EpisodePage parseEpisode(WebToonInfo info) {
		this.name = info.getTitle();
		EpisodePage episodePage = new EpisodePage(this);

		String response;
		try {
			response = requestApi();
			JSONObject json = new JSONObject(response);
			JSONArray data = json.optJSONObject("data").optJSONArray("webtoonEpisodes");
			if (data != null && data.length() > 0) {
				episodePage.episodes = parseList(info, data);
			} else {
				episodePage.episodes = new ArrayList<>();
			}
			JSONObject page = json.optJSONObject("page");

			String nick = info.getToonId();
			if (episodePage.episodes != null && !episodePage.episodes.isEmpty()) {
				episodePage.nextLink = parsePage(page, nick);
			}

			if (firstEpisodeId == 0) {
				firstEpisodeId = getFirstEpisode(nick)
					.optJSONObject("data").optInt("firstEpisodeId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return episodePage;
	}

	private JSONObject getFirstEpisode(String nick) throws Exception {
		Request.Builder builder = new Request.Builder()
			.url(PREFIX_FIRST_URL + nick);

		String response = requestApi(builder.build());
		return new JSONObject(response);
	}

	private List<Episode> parseList(WebToonInfo info, JSONArray data) {
		List<Episode> list = new ArrayList<>();
		JSONObject obj;
		Episode item;
		try {
			for (int i = 0; i < data.length(); i++) {
				obj = data.optJSONObject(i);

				item = new Episode(info, obj.optString("id"));
				item.setEpisodeTitle(obj.optString("title"));
				item.setImage(obj.optJSONObject("thumbnailImage").optString("url"));
				item.setRate(obj.optJSONObject("voteTarget").optString("voteTotalScore"));
				item.setLoginNeed(obj.optInt("price", 0) > 0);
				item.setUpdateDate(obj.optString("dateCreated"));
				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	private String parsePage(JSONObject obj, String nickName) {
		int total = obj.optInt("size", 1);
		int current = obj.optInt("no", 1);
		if (total >= current + 1) {
			// 다음 페이지 존재
			pageNo = current + 1;
			return nickName;
		} else {
			// 끝
			return null;
		}
	}

	@Override
	public String moreParseEpisode(EpisodePage item) {
		return item.nextLink;
	}

	@Override
	public Episode getFirstEpisode(Episode item) {
		Episode ret = null;
		try {
			String id = String.valueOf(firstEpisodeId);
			ret = new Episode(item);
			ret.setEpisodeId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public void init() {
		super.init();
		pageNo = 0;
	}

	@Override
	public String getMethod() {
		return POST;
	}

	@Override
	public String getUrl() {
		return EPISODE_URL;
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> map = new HashMap<>();
		map.put("page_size", "10");
		map.put("nickname", name);
		if (pageNo > 0) {
			map.put("page_no", String.valueOf(pageNo));
		}
		return map;
	}
}
