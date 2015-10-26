package com.pluu.support.tstore;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pluu.support.BaseApiImpl;
import com.pluu.webtoon.ui.BaseActivity;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.DetailView;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;
import com.pluu.webtoon.api.Status;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by nohhs on 2015-04-11.
 */
public class TStoreApi extends BaseApiImpl {

	private final String MAIN_HOST_URL = "http://m.tstore.co.kr";
	private final String HOST_URL = MAIN_HOST_URL + "/mobilepoc";
	private final String WEEKLY_URL = "http://m.tstore.co.kr/mobilepoc/webtoon/weekdayList.omp?weekday=";
	private final String MORE_EPISODE_URL = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonListMore.omp";
	private final String DETAIL_URL = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonDetail.omp?prodId=";

	private final Pattern URL_PATTERN = Pattern.compile("(?<=goInnerUrlDetail\\(\\\\\\').+(?=\\\\'\\)'\\);)");
	private final Pattern ID_PATTERN = Pattern.compile("(?<=prodId=).+(?=&)");
	private final Pattern EPISODE_ID = Pattern.compile("(?<=prodId=)\\w+");

	private Episode firstEpisode;
	private int pageNo = 1;

	public TStoreApi() {
		super(new String[]{"월", "화", "수", "목", "금", "토", "일"});
	}

	@Override
	public BaseActivity.NAV_ITEM getNaviItem() {
		return BaseActivity.NAV_ITEM.T_STORE;
	}

	@Override
	public int getMainTitleColor(Context context) {
		return context.getResources().getColor(R.color.t_store_color);
	}

	@Override
	public String getWeeklyUrl(int position) {
		return WEEKLY_URL + (position + 1);
	}

	@Override
	public List<WebToonInfo> parseMain(Context context, String url, int position) {
		ArrayList<WebToonInfo> list = new ArrayList<>();
		try {
			Document doc = getConnection(url)
								.data("weekday", String.valueOf(position + 1))
								.post();
			WebToonInfo item;
			String href;

			Matcher matcher, matcher2;
			Elements bottom = doc.select("#weekToon0" + (position + 1) + " ul li a");

			for (Element a : bottom) {
				href = a.attr("href");
				matcher = URL_PATTERN.matcher(href);
				if (!matcher.find()) {
					continue;
				}
				matcher2 = ID_PATTERN.matcher(matcher.group());
				if (!matcher2.find()) {
					continue;
				}

				item = new WebToonInfo(matcher2.group());
				item.setUrl(HOST_URL + matcher.group());
				item.setTitle(a.select(".detail dl dt").text());
				item.setImage(a.select(".thum img").last().absUrl("src"));
				item.setWriter(a.select(".txt").text());
				// TODO : Rate 갱신 체크
//				item.setRate(a.select(".cRed1").text());
				item.setUpdateDate(a.select(".grade strong").text());

				if (!a.select(".new-up").isEmpty()) {
					// 최근 업데이트
					item.setStatus(Status.UPDATE);
				}

				list.add(item);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public WebToon parseEpisode(Context context, WebToonInfo info, String url) {
		WebToon webToon = new WebToon(this, url);
		try {
			Document doc = getConnection(url).get();

			if (pageNo != 1) {
				String jsonData = getConnection(MORE_EPISODE_URL)
									   .data("prodId", info.getWebtoonId())
									   .data("currentPage", String.valueOf(pageNo))
									   .ignoreContentType(true).execute().body();
				webToon.episodes = parseList(info, new JSONObject(jsonData).optJSONArray("webtoonList"));
				pageNo++;
			} else {
				firstEpisode = getFirstItem(info, doc);
				webToon.episodes = parseList(info, url, doc);
				pageNo += 2;
			}

			if (!webToon.episodes.isEmpty()) {
				webToon.nextLink = url;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return webToon;
	}

	private List<Episode> parseList(WebToonInfo info, JSONArray array) {
		int size = array.length();
		List<Episode> list = new ArrayList<>(size);

		JSONObject obj;
		Episode item;
		for (int i = 0; i < size; i++) {
			obj = array.optJSONObject(i);

			item = new Episode(info, obj.optString("prodId"));
			item.setUrl(DETAIL_URL + item.getEpisodeId());
			item.setImage(obj.optString("filePos"));
			item.setEpisodeTitle(obj.optString("prodNm"));
			item.setUpdateDate(obj.optString("updateDate"));

			list.add(item);
		}
		return list;
	}

	private List<Episode> parseList(WebToonInfo info, String url, Document doc) {
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
				item.setUrl(HOST_URL + matcher.group());
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
		ret.setUrl(HOST_URL + matcher.group());
		return ret;
	}

	@Override
	public Detail parseDetail(Context context, Episode episode, String url) {
		Detail ret = new Detail();
		ret.webtoonId = episode.getWebtoonId();
		List<DetailView> list = new ArrayList<>();

		try {
			Document doc = getConnection(url).get();

			ret.title = doc.select(".episode-num").text();

			Matcher matcher = EPISODE_ID.matcher(url);
			if (matcher.find()) {
				ret.episodeId = matcher.group();
			}

			Elements navi = doc.select(".prev-next a");
			Pattern naviPattern = Pattern.compile("/mobilepoc.+(?='\\);)");

			Element temp = navi.first();
			if (temp.hasAttr("href")) {
				matcher = naviPattern.matcher(temp.attr("href"));
				if (matcher.find()) {
					ret.prevLink = MAIN_HOST_URL + matcher.group();
				}
			}

			temp = navi.last();
			if (temp.hasAttr("href")) {
				matcher = naviPattern.matcher(temp.attr("href"));
				if (matcher.find()) {
					ret.nextLink = MAIN_HOST_URL + matcher.group();
				}
			}

			Elements scripts = doc.select("script[type=text/javascript]");
			String html;
			for (Element script : scripts) {
				html = script.html();
				if (html.contains("bookPages")) {
					html = html.replace("\r\n", "");

					Pattern urlPattern = Pattern.compile("(?<=FILE_POS = \\\").+(?=\\\";)");
					Pattern endPattern = Pattern.compile("(?<=bookPages = Number\\(\\\")\\d+(?=\\\"\\))");

					Matcher urlMatcher = urlPattern.matcher(html);
					Matcher endMatcher = endPattern.matcher(html);

					if (!urlMatcher.find() || !endMatcher.find()) {
						continue;
					}

					String imgUrl = urlMatcher.group();
					int end = Integer.valueOf(endMatcher.group());

					for (int i = 1; i <= end; i++) {
						list.add(DetailView.createImage(MAIN_HOST_URL + imgUrl + "/" + i + ".jpg"));
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ret.list = list;
		return ret;
	}

	@Override
	public Episode getFirstEpisode(Episode item) {
		return firstEpisode;
	}

	@Override
	public void refreshEpisode() {
		super.refreshEpisode();
		pageNo = 1;
	}

	@Override
	public ShareItem getDetailShare(Episode episode, Detail detail) {
		return null;
	}
}
