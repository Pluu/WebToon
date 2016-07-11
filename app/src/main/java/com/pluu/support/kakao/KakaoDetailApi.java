package com.pluu.support.kakao;

import android.content.Context;

import com.pluu.support.impl.AbstractDetailApi;
import com.pluu.webtoon.item.Detail;
import com.pluu.webtoon.item.DetailView;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.ShareItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 카카오 페이지 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class KakaoDetailApi extends AbstractDetailApi {

	private final String DETAIL_URL = "http://page.kakao.com/viewer?productId=%s&categoryUid=10&subCategoryUid=0";
	private String id;

	public KakaoDetailApi(Context context) {
		super(context);
	}

	@Override
	public Detail parseDetail(Episode episode) {
		this.id = episode.getEpisodeId();

		Detail ret = new Detail();
		ret.webtoonId = episode.getToonId();

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

		String link = doc.select(".prevSectionBtn").attr("data-productid");
		if (!"0".equals(link)) {
			ret.prevLink = link;
		}
		link = doc.select(".nextSectionBtn").attr("data-productid");
		if (!"0".equals(link)) {
			ret.nextLink = link;
		}

		List<DetailView> list = new ArrayList<>();

		for (Element img : doc.select(".targetImg")) {
			list.add(DetailView.createImage(img.attr("data-original")));
		}
		for (Element img : doc.select(".viewWrp li input")) {
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
