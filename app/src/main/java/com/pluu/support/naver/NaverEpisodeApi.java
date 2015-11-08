package com.pluu.support.naver;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pluu.support.impl.AbstractEpisodeApi;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.EpisodePage;
import com.pluu.webtoon.item.Status;
import com.pluu.webtoon.item.WebToonInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 네이버 웹툰 Episode API
 * Created by PLUUSYSTEM-NEW on 2015-10-29.
 */
public class NaverEpisodeApi extends AbstractEpisodeApi {

	private static final String HOST_URL = "http://m.comic.naver.com/webtoon/list.nhn?titleId=%s&page=%d";
	private String webToonId;
	private int pageNo = 1;

	@Override
	public EpisodePage parseEpisode(Context context, WebToonInfo info) {
		webToonId = info.getWebtoonId();

		EpisodePage episodePage = new EpisodePage(this);

		String response;
		try {
			response = requestApi();
		} catch (Exception e) {
			e.printStackTrace();
			return episodePage;
		}

		Document doc = Jsoup.parse(response);
		episodePage.episodes = parseList(info, doc);
		episodePage.nextLink = parsePage(doc);
		pageNo++;

		return episodePage;
	}

	private List<Episode> parseList(WebToonInfo info, Document doc) {
		List<Episode> list = new ArrayList<>();
		Elements links = doc.select("#pageList a");
		Pattern pattern = Pattern.compile("no=\\d+");
		String href;
		try {
			Episode episode;
			for (Element a : links) {
				href = a.attr("href");

				Matcher matcher = pattern.matcher(href);
				if (!matcher.find()) {
					continue;
				}

				episode = new Episode(info, matcher.group().substring(3));
				episode.setEpisodeTitle(a.select(".toon_name").text());
				episode.setImage(a.select("img").first().attr("src"));
				parseToonInfo(a, episode);
				list.add(episode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private void parseToonInfo(Element doc, Episode episode) {
		Elements info = doc.select(".toon_info");
		episode.setRate(info.select("span[class=if1 st_r]").text());

		if (!info.select(".aside_info .ico_up").isEmpty()) {
			// 최근 업데이트
			episode.setStatus(Status.UPDATE);
		} else if (!info.select(".aside_info .ico_break").isEmpty()) {
			// 휴재
			episode.setStatus(Status.BREAK);
		}
	}

	private String parsePage(Document doc) {
		Elements nextPage = doc.select("#nextButton");
		if (nextPage != null && !nextPage.isEmpty()) {
			return nextPage.first().attr("href");
		}
		return null;
	}

	@Override
	public String moreParseEpisode(EpisodePage item) {
		return item.getNextLink();
	}

	@Override
	public Episode getFirstEpisode(Episode item) {
		Episode ret;
		try {
			ret = item.clone();
			ret.setEpisodeId("1");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	@Override
	public void init() {
		super.init();
		pageNo = 1;
	}

	@Override
	public String getMethod() {
		return GET;
	}

	@Override
	public String getUrl() {
		return String.format(HOST_URL, webToonId, pageNo);
	}
}
