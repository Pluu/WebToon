package com.pluu.support.naver;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pluu.support.impl.AbstractEpisodeApi;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.Status;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 네이버 웹툰 Episode API
 * Created by PLUUSYSTEM-NEW on 2015-10-29.
 */
public class NaverEpisodeApi extends AbstractEpisodeApi {

	private String URL;

	@Override
	public WebToon parseEpisode(Context context, WebToonInfo info, String url) {
		this.URL = url;

		WebToon webToon = new WebToon(this, url);

		String response;
		try {
			response = requestApi();
		} catch (Exception e) {
			e.printStackTrace();
			return webToon;
		}

		Document doc = Jsoup.parse(response);
		webToon.episodes = parseList(info, doc);
		webToon.nextLink = parsePage(doc);

		return webToon;
	}

	private List<Episode> parseList(WebToonInfo info, Document doc) {
		List<Episode> list = new ArrayList<>();
		Elements links = doc.select("#pageList a");
		Pattern pattern = Pattern.compile("no=\\d+");
		String href;
		try {
			Episode episode = null;
			for (Element a : links) {
				href = a.absUrl("href");

				Matcher matcher = pattern.matcher(href);
				if (!matcher.find()) {
					continue;
				}

				episode = new Episode(info, matcher.group().substring(3));
				String title = a.select(".toon_name").text();
				String image = a.select("img").first().attr("src");
				episode.setUrl(href);
				episode.setEpisodeTitle(title);
				episode.setImage(image);
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
			return nextPage.first().absUrl("href");
		}
		return null;
	}

	@Override
	public String moreParseEpisode(WebToon item) {
		return item.getNextLink();
	}

	@Override
	public Episode getFirstEpisode(Episode item) {
		Episode ret;
		try {
			ret = item.clone();
			ret.setUrl(item.getUrl().replaceFirst("no=\\d+", "no=1"));
			ret.setEpisodeId("1");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	@Override
	public String getMethod() {
		return GET;
	}

	@Override
	public String getUrl() {
		return URL;
	}
}
