package com.pluu.support.kakao;

import android.content.Context;

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
 * 카카오 페이지 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class KakaoDetailApi extends AbstractDetailApi {

	private final String DETAIL_URL = "http://page.kakao.com/viewer?productId=%s&categoryUid=10&subCategoryUid=0";
	private String id;

	@Override
	public Detail parseDetail(Context context, Episode episode, String url) {
		this.id = url;

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
		ret.title = doc.select("span[class=title ellipsis_hd]").text();
		ret.episodeId = id;

		String link = doc.select(".pc_view_prev").attr("data-previous");
		if (!"0".equals(link)) {
			ret.prevLink = link;
		}
		link = doc.select(".pc_view_next").attr("data-next");
		if (!"0".equals(link)) {
			ret.nextLink = link;
		}

		List<DetailView> list = new ArrayList<>();
		Elements elements = doc.select(".targetImg");
		for (Element img : elements) {
			list.add(DetailView.createImage(img.attr("data-original")));
		}
		elements = doc.select(".viewWrp li input");
		for (Element img : elements) {
			list.add(DetailView.createImage(img.attr("value")));
		}

		ret.list = list;
		return ret;
	}

	@Override
	public ShareItem getDetailShare(Episode episode, Detail detail) {
		ShareItem item = new ShareItem();
		item.title = episode.getTitle() + " / " + detail.title;
		item.url = String.format(DETAIL_URL, episode.getEpisodeId());
		return item;
	}

	@Override
	public String getMethod() {
		return GET;
	}

	@Override
	public String getUrl() {
		return String.format(DETAIL_URL, id);
	}
}
