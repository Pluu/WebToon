package com.pluu.support.daum;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pluu.support.impl.AbstractEpisodeApi;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;
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
	public WebToon parseEpisode(Context context, WebToonInfo info, String url) {
		this.name = url;
		WebToon webToon = new WebToon(this, url);

		String response;
		try {
			response = requestApi();
			JSONObject json = new JSONObject(response);
			JSONArray data = json.optJSONObject("data").optJSONArray("webtoonEpisodes");
			if (data != null && data.length() > 0) {
				webToon.episodes = parseList(info, data);
			} else {
				webToon.episodes = new ArrayList<>();
			}
			JSONObject page = json.optJSONObject("page");

			String nick = info.getWebtoonId();
			if (webToon.episodes != null && !webToon.episodes.isEmpty()) {
				webToon.nextLink = parsePage(page, nick);
			}

			if (firstEpisodeId == 0) {
				firstEpisodeId = getFirstEpisode(nick)
					.optJSONObject("data").optInt("firstEpisodeId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return webToon;
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
				item.setUrl(obj.optString("id"));
				item.setEpisodeTitle(obj.optString("title"));
				item.setImage(obj.optJSONObject("thumbnailImage").optString("url"));
				item.setRate(obj.optJSONObject("voteTarget").optString("voteTotalScore"));
				item.setIsLocked(obj.optInt("price", 0) > 0);
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
	public String moreParseEpisode(WebToon item) {
		return item.nextLink;
	}

	@Override
	public Episode getFirstEpisode(Episode item) {
		Episode ret = null;
		try {
			String id = String.valueOf(firstEpisodeId);
			ret = item.clone();
			ret.setUrl(id);
			ret.setEpisodeId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public String getMethod() {
		return POST;
	}

	@Override
	public String getId() {
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
