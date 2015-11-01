package com.pluu.support.daum;

import android.content.Context;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pluu.support.BaseApiImpl;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.DetailView;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;
import com.pluu.webtoon.api.Status;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;
import com.pluu.webtoon.common.Const;

/**
 * 다음 웹툰 Parse Api
 * Created by nohhs on 2015-03-12.
 */
public class DaumApi extends BaseApiImpl {

	private final String[] value = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
	private final String WEEKLY_URL = "http://m.webtoon.daum.net/data/mobile/webtoon?sort=update&page_no=1&week=";
	private final String EPISODE_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/list_episode_by_nickname?page_size=10&nickname=";
	private final String EPISODE_FIRST_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/view?nickname=";
	private final String DETAIL_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/viewer?id=";
	private int firstEpisodeId;

	public DaumApi() {
		super(new String[]{"월", "화", "수", "목", "금", "토", "일"});
	}

	public ServiceConst.NAV_ITEM getNaviItem() {
		return ServiceConst.NAV_ITEM.DAUM;
	}

	@Override
	public int getMainTitleColor(Context context) {
		return context.getResources().getColor(R.color.daum_color);
	}

	public String getWeeklyUrl(int position) {
		return WEEKLY_URL + value[position];
	}

	public List<WebToonInfo> parseMain(Context context, String url, int position) {
		ArrayList<WebToonInfo> list = new ArrayList<>();
		try {
			String jsonData = getConnection(url).ignoreContentType(true).execute().body();
			JSONArray array = new JSONObject(jsonData).optJSONObject("data").optJSONArray("webtoons");
			if (array != null && array.length() > 0) {
				WebToonInfo item;
				JSONObject obj, lastObj;
				String emptyAverageScore = "0.0";
				String today = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
				String date;
				for (int i = 0; i < array.length(); i++) {
					obj = array.optJSONObject(i);

					item = new WebToonInfo(obj.optString("nickname"));
					item.setUrl(EPISODE_URL + obj.optString("nickname"));
					item.setTitle(obj.optString("title"));
					lastObj = obj.optJSONObject("latestWebtoonEpisode");
					item.setImage(lastObj.optJSONObject("thumbnailImage").optString("url"));

					JSONObject info = obj.optJSONObject("cartoon").optJSONArray("artists").optJSONObject(0);
					item.setWriter(info.optString("name"));
					item.setRate(Const.getRateNameByRate(obj.optString("averageScore")));
					if (TextUtils.equals(emptyAverageScore, item.getRate())) {
						item.setRate(null);
					}

					date = lastObj.optString("dateCreated");
					item.setUpdateDate(
						date.substring(2, 4) + "." + date.substring(4, 6) + "." + date.substring(6,
																								 8));
					if (today.equals(date.substring(0, 8))) {
						// 최근 업데이트
						item.setStatus(Status.UPDATE);
					} else if ("Y".equals(obj.optString("restYn"))){
						// 휴재
						item.setStatus(Status.BREAK);
					}

					item.setIsAdult(obj.optInt("ageGrade") == 19);
					list.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public WebToon parseEpisode(Context context, WebToonInfo info, String url) {
		WebToon webToon = new WebToon(this, url);
		try {
			String jsonData = getConnection(url).ignoreContentType(true).execute().body();
			JSONObject json = new JSONObject(jsonData);
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

			String infoJson = getConnection(EPISODE_FIRST_URL + nick).ignoreContentType(true).execute().body();
			firstEpisodeId = new JSONObject(infoJson).optJSONObject("data").optInt("firstEpisodeId");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webToon;
	}

	private List<Episode> parseList(WebToonInfo info, JSONArray data) {
		List<Episode> list = new ArrayList<>();
		JSONObject obj;
		Episode item;
		try {
			for (int i = 0; i < data.length(); i++) {
				obj = data.optJSONObject(i);

				item = new Episode(info, obj.optString("id"));
				item.setUrl(DETAIL_URL + obj.optString("id"));
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
			return EPISODE_URL + nickName + "&page_no=" + (current + 1);
		} else {
			// 끝
			return null;
		}
	}

	public Detail parseDetail(Context context, Episode episode, String url) {
		Detail ret = new Detail();
		ret.webtoonId = episode.getWebtoonId();

		List<DetailView> list = new ArrayList<>();
		try {
			String jsonData = getConnection(url).ignoreContentType(true).execute().body();
			JSONObject json = new JSONObject(jsonData).optJSONObject("data");
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

	public Episode getFirstEpisode(Episode item) {
		Episode ret;
		try {
			ret = item.clone();
			ret.setUrl(DETAIL_URL + firstEpisodeId);
			ret.setEpisodeId(String.valueOf(firstEpisodeId));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	public ShareItem getDetailShare(Episode episode, Detail detail) {
		ShareItem item = new ShareItem();
		item.title = episode.getTitle() + " / " + detail.title;
		item.url = "http://m.webtoon.daum.net/m/webtoon/viewer/" + detail.episodeId;
		return item;
	}
}
