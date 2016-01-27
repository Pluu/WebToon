package com.pluu.support.daum;

import com.pluu.support.impl.AbstractDetailApi;
import com.pluu.webtoon.item.DETAIL_TYPE;
import com.pluu.webtoon.item.Detail;
import com.pluu.webtoon.item.DetailView;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.ShareItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 다음 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-30.
 */
public class DaumDetailApi extends AbstractDetailApi {

	private static final String DETAIL_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/viewer";
	private static final String SHARE_URL = "http://m.webtoon.daum.net/m/webtoon/viewer/";
	private String id;

	@Override
	public Detail parseDetail(Episode episode) {
		this.id = episode.getEpisodeId();

		Detail ret = new Detail();
		ret.webtoonId = episode.getToonId();

		List<DetailView> list = null;
		try {
			String response = requestApi();
			JSONObject json = new JSONObject(response).optJSONObject("data");
			JSONObject info = json.optJSONObject("webtoonEpisode");
			ret.title = info.optString("title");
			ret.episodeId = info.optString("id");

			int nextId = json.optInt("nextEpisodeId", 0);
			int prevId = json.optInt("prevEpisodeId", 0);
			if (nextId > 0) {
				ret.nextLink = String.valueOf(nextId);
			}
			if (prevId > 0) {
				ret.prevLink = String.valueOf(prevId);
			}

			if (info.isNull("multiType")) {
				list = defaultDetailParse(json);
			} else {
				list = chattingDetailParse(json);
				ret.type = DETAIL_TYPE.DAUM_CHATTING;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.list = list;
		return ret;
	}

	private List<DetailView> defaultDetailParse(JSONObject json) {
		List<DetailView> list = new ArrayList<>();
		JSONArray array = json.optJSONArray("webtoonImages");
		for (int i = 0; i < array.length(); i++) {
            list.add(DetailView.createImage(array.optJSONObject(i).optString("url")));
        }
		array = json.optJSONArray("webtoonEpisodePages");
		for (int i = 0; i < array.length(); i++) {
            list.add(DetailView.createImage(array.optJSONObject(i)
                    .optJSONArray("webtoonEpisodePageMultimedias")
                    .optJSONObject(0)
                    .optJSONObject("image")
                    .optString("url")));
        }
		return list;
	}

	private List<DetailView> chattingDetailParse(JSONObject json) {
		List<DetailView> list = new ArrayList<>();
		JSONArray array = json.optJSONArray("webtoonEpisodeChattings");
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.optJSONObject(i);
			String type = object.optString("messageType");
			if ("notice".equals(type)) {
				if (object.isNull("message")) {
					list.add(DetailView.createChatNoticeImage(object.optJSONObject("image").optString("url")));
				} else {
					list.add(DetailView.createChatNotice(object.optString("message")));
				}
			} else if ("another".equals(type)) {
				list.add(DetailView.createChatLeft(
						object.optJSONObject("profileImage").optString("url"),
						object.optString("profileName"),
						object.optString("message"))
				);
			} else if ("own".equals(type)) {
				list.add(DetailView.createChatRight(
						object.optJSONObject("profileImage").optString("url"),
						object.optString("profileName"),
						object.optString("message"))
				);
			}
		}
		if (!list.isEmpty()) {
			list.add(0, DetailView.createChatEmpty());
			list.add(DetailView.createChatEmpty());
		}
		return list;
	}

	@Override
	public ShareItem getDetailShare(Episode episode, Detail detail) {
		ShareItem item = new ShareItem();
		item.title = episode.getTitle() + " / " + detail.title;
		item.url = SHARE_URL + detail.episodeId;
		return item;
	}

	@Override
	public String getMethod() {
		return POST;
	}

	@Override
	public String getUrl() {
		return DETAIL_URL;
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> map = new HashMap<>();
		map.put("id", id);
		return map;
	}
}
