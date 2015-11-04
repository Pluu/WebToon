package com.pluu.support.kakao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.pluu.support.impl.AbstractEpisodeApi;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;
import com.squareup.okhttp.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 카카오 페이지 웹툰 Episode API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class KakaoEpisodeApi extends AbstractEpisodeApi {

	private final String FIRST_EPISODE= "http://page.kakao.com/home/%s?categoryUid=10&subCategoryUid=0&navi=1&inkatalk=0";
	private final String EPISODE_URL = "http://page.kakao.com/home/singlelist?seriesId=%s&offset=%d&navi=1&inkatalk=0";

	private Pattern pattern = Pattern.compile("(?<=productId=)\\d+");
	private final int SIZE = 25;

	private String url;
	private int offset;
	private Episode firstEpisode;

	@Override
	public WebToon parseEpisode(Context context, WebToonInfo info, String url) {
		this.url = String.format(EPISODE_URL, info.getWebtoonId(), offset);

		WebToon webToon = new WebToon(this, url);

		String response;
		try {
			response = requestApi();
		} catch (Exception e) {
			e.printStackTrace();
			return webToon;
		}

		Document doc = Jsoup.parse(response);

		if (offset == 0) {
			try {
				firstEpisode = getFirstItem(info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		webToon.episodes = parseList(info, url, doc);
		if (!webToon.episodes.isEmpty()) {
			webToon.nextLink = parsePage(info);
		}

		return webToon;
	}

	private Episode getFirstItem(WebToonInfo info) throws Exception {
		Request.Builder builder = new Request.Builder()
			.url(String.format(FIRST_EPISODE, info.getWebtoonId()));
		String response = requestApi(builder.build());
		Document doc = Jsoup.parse(response);
		String id = doc.select(".firstViewBtn").attr("data-productId");
		Episode ret= new Episode(info, id);
		ret.setUrl(id);
		return ret;
	}

	private List<Episode> parseList(WebToonInfo info, String url, Document doc) {
		List<Episode> list = new ArrayList<>();
		Elements links = doc.select("li[class=list viewerList]");
		Episode item;

		try {
			for (Element a : links) {
				item = new Episode(info, a.select(".productId").attr("value"));
				item.setUrl(item.getEpisodeId());
				item.setImage(a.select(".thum").attr("src"));
				item.setEpisodeTitle(a.select("span[class=title ellipsis_hd]").text());
				item.setUpdateDate(a.select(".date").text());
				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	private String parsePage(WebToonInfo info) {
		offset += SIZE;
		return info.getUrl();
	}

	@Override
	public String moreParseEpisode(WebToon item) {
		return item.nextLink;
	}

	@Override
	public Episode getFirstEpisode(Episode item) {
		return firstEpisode;
	}

	@Override
	public void init() {
		super.init();
		offset = 0;
	}

	@Override
	public String getMethod() {
		return GET;
	}

	@Override
	public String getUrl() {
		return url;
	}
}
