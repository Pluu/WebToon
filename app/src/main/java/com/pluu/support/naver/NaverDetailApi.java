package com.pluu.support.naver;

import android.content.Context;
import android.text.TextUtils;

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
 * 네이버 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-29.
 */
public class NaverDetailApi extends AbstractDetailApi {

	private final String SHARE_URL = "http://m.comic.naver.com/webtoon/detail.nhn?titleId=%s&no=%s";
	private String url;


	private final List<String> SKIP_DETAIL = new ArrayList<String>() {{
		add("http://static.naver.com/m/comic/im/txt_ads.png");
		add("http://static.naver.com/m/comic/im/toon_app_pop.png");
	}};

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
			return ret;
		}

		Document doc = Jsoup.parse(response);
		Pattern pattern = Pattern.compile("no=\\d+");
		ret.title = doc.select("div[class=chh] span, h1[class=tit]").first().text();

		Elements cutToonElements = doc.select("a[class=prev smt-btn-prev-cut]");
		if (cutToonElements != null && !cutToonElements.isEmpty()) {
			// 스마트툰
			ret.list = parseDetailSmartToonType(doc);

			// 이전, 다음화
			Elements navi = doc.select(".viewer_inner");
			Elements temp = navi.select("a[class=btn_next]");
			if (temp != null && !temp.isEmpty()) {
				ret.nextLink = temp.first().absUrl("href");
			}
			temp = navi.select("a[class=btn_prev]");
			if (temp != null && !temp.isEmpty()) {
				ret.prevLink = temp.first().absUrl("href");
			}
		} else {
			cutToonElements = doc.select("div[class=viewer cuttoon]");
			if (cutToonElements != null && !cutToonElements.isEmpty()) {
				// 컷툰
				ret.list = parseDetailCutToonType(doc);

				// 이전, 다음화
				Elements navi = doc.select(".paging_wrap");
				Elements temp = navi.select("a[class=pg_next]");
				if (temp != null && !temp.isEmpty()) {
					ret.nextLink = temp.first().absUrl("href");
				}
				temp = navi.select("a[class=pg_prev]");
				if (temp != null && !temp.isEmpty()) {
					ret.prevLink = temp.first().absUrl("href");
				}
			} else {
				// 일반 웹툰
				ret.list = parseDetailNormalType(doc);

				// 이전, 다음화
				for (Element element : doc.select("div[class=sc2] a")) {
					if (!element.select("span[class=nx]").isEmpty()) {
						ret.nextLink = element.absUrl("href");
					} else if (!element.select("span[class=pv]").isEmpty()) {
						ret.prevLink = element.absUrl("href");
					}
				}
			}
		}

		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			ret.episodeId = matcher.group().substring(3);
		}

		return ret;
	}

	private List<DetailView> parseDetailCutToonType(Document doc) {
		List<DetailView> list = new ArrayList<>();

		Pattern pattern = Pattern.compile("(?<=sImageUrl :')https?://(\\w*:\\w*@)?[-\\w.]+(:\\d+)?(/([\\w/_.]*(\\?\\S+)?)?)?(?=', sWatermarkUrl)");
		Matcher matcher = pattern.matcher(doc.select("script[type=text/javascript]").last().html().replace("\r\n", ""));
		while (matcher.find()) {
			list.add(DetailView.createImage(matcher.group()));
		}
		return list;
	}

	private List<DetailView> parseDetailSmartToonType(Document doc) {
		List<DetailView> list = new ArrayList<>();

		Pattern pattern = Pattern.compile("(?<=sImageUrl:')https?://(\\w*:\\w*@)?[-\\w.]+(:\\d+)?(/([\\w/_.]*(\\?\\S+)?)?)?(?=',sEffectType)");
		Matcher matcher = pattern.matcher(doc.select("script[type=text/javascript]").last().html().replace("\r\n", ""));
		while (matcher.find()) {
			list.add(DetailView.createImage(matcher.group()));
		}
		return list;
	}

	private List<DetailView> parseDetailNormalType(Document doc) {
		List<DetailView> list = new ArrayList<>();
		Elements links = doc.select("#toonLayer li img[class=fx2], #ct img, .pop_ct img");
		String path;
		for (Element item : links) {
			path = item.attr("data-lazy-src");
			if (TextUtils.isEmpty(path)) {
				path = item.attr("src");
			}
			if (!TextUtils.isEmpty(path)
				&& !SKIP_DETAIL.contains(path)) {
				list.add(DetailView.createImage(path));
			}
		}
		return list;
	}

	@Override
	public ShareItem getDetailShare(Episode episode, Detail detail) {
		String url = String.format(SHARE_URL, detail.webtoonId, detail.episodeId);
		ShareItem item = new ShareItem();
		item.title = episode.getTitle() + " / " + detail.title;
		item.url = url;
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
