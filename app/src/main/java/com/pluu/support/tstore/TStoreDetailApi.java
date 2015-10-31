package com.pluu.support.tstore;

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
 * TStore 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class TStoreDetailApi extends AbstractDetailApi {

	private static final String MAIN_HOST_URL = "http://m.tstore.co.kr";
	private static final String DETAIL_URL = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonDetail.omp?prodId=";
	private static final Pattern EPISODE_ID = Pattern.compile("(?<=prodId=)\\w+");

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
		ret.title = doc.select(".episode-num").text();

		Matcher matcher = EPISODE_ID.matcher(url);
		if (matcher.find()) {
			ret.episodeId = matcher.group();
		}

		Elements navi = doc.select(".prev-next a");
		Pattern naviPattern = Pattern.compile("/mobilepoc.+(?='\\);)");

		Element temp = navi.first();
		if (temp.hasAttr("href")) {
			matcher = naviPattern.matcher(temp.attr("href"));
			if (matcher.find()) {
				ret.prevLink = MAIN_HOST_URL + matcher.group();
			}
		}

		temp = navi.last();
		if (temp.hasAttr("href")) {
			matcher = naviPattern.matcher(temp.attr("href"));
			if (matcher.find()) {
				ret.nextLink = MAIN_HOST_URL + matcher.group();
			}
		}

		List<DetailView> list = new ArrayList<>();
		Elements scripts = doc.select("script[type=text/javascript]");
		String html;
		for (Element script : scripts) {
			html = script.html();
			if (html.contains("bookPages")) {
				html = html.replace("\r\n", "");

				Pattern urlPattern = Pattern.compile("(?<=FILE_POS = \\\").+(?=\\\";)");
				Pattern endPattern = Pattern.compile("(?<=bookPages = Number\\(\\\")\\d+(?=\\\"\\))");

				Matcher urlMatcher = urlPattern.matcher(html);
				Matcher endMatcher = endPattern.matcher(html);

				if (!urlMatcher.find() || !endMatcher.find()) {
					continue;
				}

				String imgUrl = urlMatcher.group();
				int end = Integer.valueOf(endMatcher.group());

				for (int i = 1; i <= end; i++) {
					list.add(DetailView.createImage(MAIN_HOST_URL + imgUrl + "/" + i + ".jpg"));
				}
				break;
			}
		}

		ret.list = list;
		return ret;
	}

	@Override
	public ShareItem getDetailShare(Episode episode, Detail detail) {
		return null;
	}

	@Override
	public String getMethod() {
		return GET;
	}

	@Override
	public String getUrl() {
		return DETAIL_URL + url;
	}
}
