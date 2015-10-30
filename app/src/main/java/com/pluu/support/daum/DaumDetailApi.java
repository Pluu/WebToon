package com.pluu.support.daum;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pluu.support.impl.AbstractDetailApi;
import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.DetailView;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;

/**
 * 다음 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-30.
 */
public class DaumDetailApi extends AbstractDetailApi {

	public static final String DETAIL_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/viewer?id=";
	private final String SHARE_URL = "http://m.webtoon.daum.net/m/webtoon/viewer/";
	private String url;

	@Override
	public Detail parseDetail(Context context, Episode episode, String url) {
		this.url = url;

		Detail ret = new Detail();
		ret.webtoonId = episode.getWebtoonId();

		List<DetailView> list = new ArrayList<>();
		try {
			String response = requestApi();
			JSONObject json = new JSONObject(response).optJSONObject("data");
			JSONObject info = json.optJSONObject("webtoonEpisode");
			ret.title = info.optString("title");
			ret.episodeId = info.optString("id");

			int nextId = json.optInt("nextEpisodeId", 0);
			int prevId = json.optInt("prevEpisodeId", 0);
			if (nextId > 0) {
				ret.nextLink = DETAIL_URL + nextId;
			}
			if (prevId > 0) {
				ret.prevLink = DETAIL_URL + prevId;
			}
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.list = list;
		return ret;
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
		return url;
	}
}
