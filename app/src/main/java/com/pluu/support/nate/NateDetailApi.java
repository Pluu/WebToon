package com.pluu.support.nate;

import java.util.ArrayList;
import java.util.List;

import com.pluu.support.impl.AbstractDetailApi;
import com.pluu.webtoon.item.Detail;
import com.pluu.webtoon.item.DetailView;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.ShareItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 네이트 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class NateDetailApi extends AbstractDetailApi {

	private final String DETAIL_URL = "http://m.comics.nate.com/view/show?btno=%s&bsno=%s&order=up";
	private String webToonId, episodeId;

	@Override
	public Detail parseDetail(Episode episode) {
		this.webToonId = episode.getWebtoonId();
		this.episodeId = episode.getEpisodeId();

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
		ret.episodeId = episodeId;

		Elements temp = doc.select(".btn_prev");
		if (temp != null && !temp.isEmpty()) {
			ret.prevLink = "/view/" + temp.first().attr("href");
		}

		temp = doc.select(".btn_next");
		if (temp != null && !temp.isEmpty()) {
			ret.nextLink = "/view/" + temp.first().attr("href");
		}

		List<DetailView> list = new ArrayList<>();
		Elements elements = doc.select(".fview");
		for (Element img : elements) {
			list.add(DetailView.createImage(img.attr("src")));
		}

		ret.list = list;
		return ret;
	}

	@Override
	public ShareItem getDetailShare(Episode episode, Detail detail) {
		ShareItem item = new ShareItem();
		item.title = episode.getTitle() + " / " + detail.title;
		item.url = String.format(DETAIL_URL, episode.getWebtoonId(), episode.getEpisodeId());
		return item;
	}

	@Override
	public String getMethod() {
		return GET;
	}

	@Override
	public String getUrl() {
		return String.format(DETAIL_URL, webToonId, episodeId);
	}

}
