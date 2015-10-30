package com.pluu.support.nate;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pluu.support.impl.AbstractDetailApi;
import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.DetailView;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 네이트 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class NateDetailApi extends AbstractDetailApi {

	private final Pattern EPISODE_ID_PATTERN = Pattern.compile("(?<=bsno=)\\d+");

	private String url;

	@Override
	public Detail parseDetail(Context context, Episode episode, String url) {
		this.url = url;

		Detail ret = new Detail();
		ret.webtoonId = episode.getWebtoonId();

		String response;
		try {
			response = requestApi();
		} catch (Exception e) {
			e.printStackTrace();
			ret.list = new ArrayList<>();
			return ret;
		}

		Document doc = Jsoup.parse(response);
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

		List<DetailView> list = new ArrayList<>();
		Elements elements = doc.select(".fview");
		for (Element img : elements) {
			list.add(DetailView.createImage(img.absUrl("src")));
		}

		ret.list = list;
		return ret;
	}

	@Override
	public ShareItem getDetailShare(Episode episode, Detail detail) {
		ShareItem item = new ShareItem();
		item.title = episode.getTitle() + " / " + detail.title;
		item.url = episode.getUrl();
		return item;
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
