package com.pluu.support.olleh;

import android.content.Context;

import com.pluu.support.impl.AbstractDetailApi;
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

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 올레 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class OllehDetailApi extends AbstractDetailApi {

	private final String DETAIL_URL = OllehWeekApi.HOST + "/api/work/getTimesListByWork.kt";
	private final String DETAIL_IMG_URL = OllehWeekApi.HOST + "/api/work/getTimesDetailImageList.kt";
	private final String SHARE_URL = OllehWeekApi.HOST + "/web/times_view.kt?webtoonseq=%s&timesseq=%s";

	private String wettonId, timesseq;

	public OllehDetailApi(Context context) {
		super(context);
	}

	@Override
	public Detail parseDetail(Episode episode) {
		this.wettonId = episode.getToonId();
		this.timesseq = episode.getEpisodeId();

		Detail ret = new Detail();
		ret.webtoonId = wettonId;

		JSONObject obj;
		try {
			String response = requestApi();
			obj = new JSONObject(response);

			JSONArray array = obj.optJSONArray("timesList");
			int timeSeq = Integer.valueOf(timesseq);
			for (int i = 0; i < array.length(); i++) {
				obj = array.optJSONObject(i);
				if (timeSeq == obj.optInt("timesseq")) {
					ret.episodeId = obj.optString("timesseq");
					ret.title = obj.optString("timestitle");
					if (i - 1 >= 0) {
						ret.nextLink = String.valueOf(array.optJSONObject(i - 1).optInt("timesseq"));
					}
					if (i + 1 < array.length()) {
						ret.prevLink = String.valueOf(array.optJSONObject(i + 1).optInt("timesseq"));
					}
					break;
				}
			}

			ret.list = parserToon(getRequestImg());
		} catch (Exception e) {
			e.printStackTrace();
			ret.list = new ArrayList<>();
			return ret;
		}
		return ret;
	}

	private JSONObject getRequestImg() throws Exception {
		Request.Builder builder = new Request.Builder()
			.url(DETAIL_IMG_URL);

		for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
			builder.addHeader(entry.getKey(), entry.getValue());
		}

		FormBody.Builder formBuilder = new FormBody.Builder();
		for (Map.Entry<String, String> entry : getParams().entrySet()) {
			formBuilder.add(entry.getKey(), entry.getValue());
		}
		RequestBody requestBody = formBuilder.build();
		builder.post(requestBody);

		String response = requestApi(builder.build());
		return new JSONObject(response);
	}

	private List<DetailView> parserToon(JSONObject obj) {
		List<DetailView> list = new ArrayList<>();
		JSONArray array = obj.optJSONArray("imageList");
		JSONObject obj2;
		for (int i = 0; i < array.length(); i++) {
			obj2 = array.optJSONObject(i);
			list.add(DetailView.createImage(obj2.optString("imagepath")));
		}
		return list;
	}

	@Override
	public ShareItem getDetailShare(Episode episode, Detail detail) {
		ShareItem item = new ShareItem();
		item.title = episode.getTitle() + " / " + detail.title;
		item.url = String.format(SHARE_URL, detail.webtoonId, detail.episodeId);
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
	public Map<String, String> getHeaders() {
		Map<String, String> map = new HashMap<>();
		map.put("Referer", OllehWeekApi.HOST);
		return map;
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> map = new HashMap<>();
		map.put("mobileyn", "N");
		map.put("webtoonseq", wettonId);
		map.put("timesseq", timesseq);
		return map;
	}

}
