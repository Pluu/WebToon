package com.pluu.support.olleh;

import android.content.Context;

import com.pluu.support.impl.AbstractEpisodeApi;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.EpisodePage;
import com.pluu.webtoon.item.WebToonInfo;
import com.pluu.webtoon.item.WebToonType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 올레 웹툰 Episode API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class OllehEpisodeApi extends AbstractEpisodeApi {

	private static final String EPISODE_URL = "http://webtoon.olleh.com/api/work/getTimesListByWork.kt";
	private final int PAGE_SIZE = 20;

	private JSONArray savedArray;
	private int totalSize;

	private Episode firstEpisode;
	private int page = 0;

	private String id;

	public OllehEpisodeApi(Context context) {
		super(context);
	}

	@Override
	public EpisodePage parseEpisode(WebToonInfo info) {
		id = info.getToonId();

		EpisodePage episodePage = new EpisodePage(this);

		try {
			String response;
			if (savedArray == null) {
				response = requestApi();
				savedArray = new JSONObject(response).optJSONArray("timesList");
				totalSize = savedArray.length();
				if (savedArray != null) {
					totalSize = savedArray.length();
					firstEpisode
						= createEpisode(info, savedArray.optJSONObject(totalSize - 1));
				}
			}

			if (totalSize > 0) {
				episodePage.episodes = parseList(info, savedArray, page);
				episodePage.nextLink = getNextPageLink(totalSize, page);
			}
			page++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return episodePage;
	}

	private List<Episode> parseList(WebToonInfo info, JSONArray array, int page) {
		List<Episode> list = new ArrayList<>();

		int startPage = page * PAGE_SIZE;
		int endPage = (page + 1) * PAGE_SIZE;
		if (endPage > totalSize) {
			endPage = totalSize;
		}

		for (int i = startPage; i < endPage; i++) {
			list.add(createEpisode(info, array.optJSONObject(i)));
		}
		return list;
	}

	private Episode createEpisode(WebToonInfo info, JSONObject obj) {
		Episode item = new Episode(info, obj.optString("timesseq"));
		item.setEpisodeTitle(obj.optString("timestitle"));
		item.setImage(info.getType() == WebToonType.TOON
						  ? obj.optString("thumbpath")
						  : info.getImage());
		item.setRate(obj.optString("totalstickercnt"));
//				item.setUpdateDate(obj.optString("regdt"));

		return item;
	}

	private String getNextPageLink(int totalSize, int current) {
		int total = (int) Math.ceil(totalSize / (double) PAGE_SIZE);
		if (total >= current + 1) {
			// 다음 페이지 존재
			return EPISODE_URL;
		}

		return null;
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
		savedArray = null;
		page = 0;
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
	public Map<String, String> getHeaders() {
		Map<String, String> map = new HashMap<>();
		map.put("Referer", "http://webtoon.olleh.com");
		return map;
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> map = new HashMap<>();
		map.put("mobileyn", "N");
		map.put("toonfg", "toon");
		map.put("sort", "subject");
		map.put("webtoonseq", id);
		return map;
	}

}
