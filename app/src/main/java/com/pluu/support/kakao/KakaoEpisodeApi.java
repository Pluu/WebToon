package com.pluu.support.kakao;

import com.pluu.support.impl.AbstractEpisodeApi;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.EpisodePage;
import com.pluu.webtoon.item.WebToonInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 카카오 페이지 웹툰 Episode API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class KakaoEpisodeApi extends AbstractEpisodeApi {

	private final String EPISODE_URL = "http://page.kakao.com/home/%s?categoryUid=10&subCategoryUid=1000&navi=1&inkatalk=0";
	private final String MORE_EPISODE_URL = "http://page.kakao.com/home/ajaxCallHomeSingleList?seriesId=%s&page=%d&navi=1&inkatalk=0";

	private String url;
	private int offset;
	private Episode firstEpisode;

	@Override
	public EpisodePage parseEpisode(WebToonInfo info) {
		if (offset > 0) {
			this.url = String.format(MORE_EPISODE_URL, info.getToonId(), offset);
		} else {
			this.url = String.format(EPISODE_URL, info.getToonId());
		}

		EpisodePage episodePage = new EpisodePage(this);

		String response;
		try {
			response = requestApi();
		} catch (Exception e) {
			e.printStackTrace();
			return episodePage;
		}

		Document doc = Jsoup.parse(response);

		if (offset == 0) {
			try {
				firstEpisode = getFirstItem(info, doc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		episodePage.episodes = parseList(info, doc);
		if (!episodePage.episodes.isEmpty()) {
			offset++;
			episodePage.nextLink = info.getToonId();
		}

		return episodePage;
	}

	private Episode getFirstItem(WebToonInfo info, Document doc) throws Exception {
		String id = doc.select(".topContentWrp .viewerLinkBtn").attr("data-productId");
		return new Episode(info, id);
	}

	private List<Episode> parseList(WebToonInfo info, Document doc) {
		List<Episode> list = new ArrayList<>();
		Elements links = doc.select(".list");
		Episode item;

		try {
			for (Element a : links) {
				item = new Episode(info, a.attr("data-productid"));
				item.setImage(a.select(".thumbnail img").attr("src"));
				item.setEpisodeTitle(a.select(".title").text());
				item.setUpdateDate(a.select(".date").text());
				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
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

	@Override
	public Map<String, String> getHeaders() {
		Map<String, String> map = new HashMap<>();
		map.put("User-Agent", "Mozilla/5.0 (Linux; Android 4.4.2; Nexus 4 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Mobile Safari/537.36");
		return map;
	}
}
