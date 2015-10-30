package com.pluu.support.kakao;

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
 * 카카오 페이지 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class KakaoDetailApi extends AbstractDetailApi {

	private final String DETAIL_URL = "http://page.kakao.com/viewer?productId=%s&categoryUid=10&subCategoryUid=0";
	private Pattern pattern = Pattern.compile("(?<=productId=)\\d+");
	private String url;

	@Override
	public Detail parseDetail(Context context, Episode episode, String url) {
		this.url = String.format(DETAIL_URL, url);

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
		ret.title = doc.select(".productTitle").text();

		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			ret.episodeId = matcher.group();
		}

		Element menuDoc = doc.select("div[class=remoteBar clearfix]").first();
		matcher = pattern.matcher(menuDoc.select(".prevBtn").attr("data-href"));
		if (matcher.find()) {
			if (!"0".equals(matcher.group())) {
				ret.prevLink = String.format(DETAIL_URL, matcher.group());
			}
		}
		matcher = pattern.matcher(menuDoc.select(".nextBtn").attr("data-href"));
		if (matcher.find()) {
			if (!"0".equals(matcher.group())) {
				ret.nextLink = String.format(DETAIL_URL, matcher.group());
			}
		}

		List<DetailView> list = new ArrayList<>();
		Elements elements = doc.select(".targetImg");
		for (Element img : elements) {
			list.add(DetailView.createImage(img.absUrl("data-original")));
		}
		elements = doc.select(".viewWrp li input");
		for (Element img : elements) {
			list.add(DetailView.createImage(img.absUrl("value")));
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
