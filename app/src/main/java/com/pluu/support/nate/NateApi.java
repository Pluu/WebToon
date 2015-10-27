package com.pluu.support.nate;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pluu.support.BaseApiImpl;
import com.pluu.webtoon.ui.BaseActivity;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.DetailView;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 네이트 웹툰 API
 * Created by nohhs on 2015-04-11.
 */
public class NateApi extends BaseApiImpl {

	private final String WEEKLY_URL = "http://m.comics.nate.com/main/index";
	private final String MORE_EPISODE_URL = "http://m.comics.nate.com/rest/infoList?btno=%s&page=%d";

	private final Pattern EPISODE_ID_PATTERN = Pattern.compile("(?<=bsno=)\\d+");

	private Episode firstEpisode;
	private int pageNo = 1;

	public NateApi() {
		super(new String[]{"월", "화", "수", "목", "금", "토", "일"});
	}

	public BaseActivity.NAV_ITEM getNaviItem() {
		return BaseActivity.NAV_ITEM.NATE;
	}

	@Override
	public int getMainTitleColor(Context context) {
		return context.getResources().getColor(R.color.nate_color);
	}

	public String getWeeklyUrl(int position) {
		return WEEKLY_URL;
	}

	public List<WebToonInfo> parseMain(Context context, String url, int position) {
		ArrayList<WebToonInfo> list = new ArrayList<>();
		try {
			Document doc = getConnection(url).get();
			Elements links = doc.select(".wkTypeAll_" + position);
			WebToonInfo item = null;
			Pattern pattern = Pattern.compile("(?<=btno=)\\d+");
			String href;
			for (Element a : links) {
				href = a.absUrl("href");
				Matcher matcher = pattern.matcher(href);
				if (!matcher.find()) {
					continue;
				}

				item = new WebToonInfo(matcher.group());
				item.setUrl(href);
				item.setTitle(a.select(".wtl_title").text());
				item.setImage(a.select(".wtl_img img").first().attr("src"));
				item.setWriter(a.select(".wtl_author").text());
				list.add(item);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public WebToon parseEpisode(Context context, WebToonInfo info, String url) {
		WebToon webToon = new WebToon(this, url);
		try {
			if (pageNo > 1) {
				String jsonData = getConnection(
					String.format(MORE_EPISODE_URL, info.getWebtoonId(), pageNo++))
									   .ignoreContentType(true).execute().body();
				webToon.episodes = parseList(info, new JSONArray(jsonData));
			} else {
				Document doc = getConnection(url).get();
				if (firstEpisode == null) {
					String href = doc.select(".btn_1stEpisode").first().absUrl("href");
					Matcher matcher = EPISODE_ID_PATTERN.matcher(href);
					if (matcher.find()) {
						firstEpisode = new Episode(info, matcher.group());
						firstEpisode.setUrl(href);
					}
				}

				webToon.episodes = parseList(info, url, doc);
				pageNo++;
			}

			if (!webToon.episodes.isEmpty()) {
				webToon.nextLink = url;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			webToon.episodes = new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webToon;
	}

	private List<Episode> parseList(WebToonInfo info, String url, Document doc) {
		List<Episode> list = new ArrayList<>();
		Elements links = doc.select(".first");
		Episode item;

		try {
			Matcher matcher;
			String href;

			for (Element a : links) {
				href = a.select("a").first().absUrl("href");

				matcher = EPISODE_ID_PATTERN.matcher(href);
				if (!matcher.find()) {
					continue;
				}

				item = new Episode(info, matcher.group());
				item.setUrl(href);
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

	public Detail parseDetail(Context context, Episode episode, String url) {
		Detail ret = new Detail();
		ret.webtoonId = episode.getWebtoonId();
		List<DetailView> list = new ArrayList<>();

		try {
			Document doc = getConnection(url).get();

			ret.title = doc.select(".tvi_header").text();

			Matcher matcher = EPISODE_ID_PATTERN.matcher(url);
			if (matcher.find()) {
				ret.episodeId = matcher.group();
			}

			Elements temp = doc.select(".btn_prev");
			if (temp != null && !temp.isEmpty()) {
				ret.prevLink = temp.first().absUrl("href");
			}

			temp = doc.select(".btn_next");
			if (temp != null && !temp.isEmpty()) {
				ret.nextLink = temp.first().absUrl("href");
			}

			Elements elements = doc.select(".fview");
			for (Element img : elements) {
				list.add(DetailView.createImage(img.absUrl("src")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ret.list = list;
		return ret;
	}

	public Episode getFirstEpisode(Episode item) {
		return firstEpisode;
	}

	@Override
	public void refreshEpisode() {
		super.refreshEpisode();
		pageNo = 1;
	}

	public ShareItem getDetailShare(Episode episode, Detail detail) {
		ShareItem item = new ShareItem();
		item.title = episode.getTitle() + " / " + detail.title;
		item.url = episode.getUrl();
		return item;
	}
}
