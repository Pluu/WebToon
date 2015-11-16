package com.pluu.support.tstore;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * TStore 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-31.
 */
public class TStoreDetailApi extends AbstractDetailApi {

	private static final String MAIN_HOST_URL = "http://m.tstore.co.kr";
	private static final String DETAIL_URL = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonDetail.omp?prodId=";
	private static final Pattern EPISODE_ID = Pattern.compile("(?<=prodId=)\\w+");
	private static final String SHARE_URL = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonDetail.omp?prodId=%s&PrePageNm=/detail/webtoon/mw";

	private String id;

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
		ret.title = doc.select(".episode-num").text();
		ret.episodeId = id;

		Matcher matcher;
		Elements navi = doc.select(".prev-next a");

		Element temp = navi.first();
		if (temp.hasAttr("href")) {
			matcher = EPISODE_ID.matcher(temp.attr("href"));
			if (matcher.find()) {
				ret.prevLink = matcher.group();
			}
		}

		temp = navi.last();
		if (temp.hasAttr("href")) {
			matcher = EPISODE_ID.matcher(temp.attr("href"));
			if (matcher.find()) {
				ret.nextLink = matcher.group();
			}
		}

		List<DetailView> list = new ArrayList<>();
		Elements scripts = doc.select("script[type=text/javascript]");
		String html;
		Pattern urlPattern = Pattern.compile("(?<=FILE_POS = \\\").+(?=\\\";)");
		Pattern endPattern = Pattern.compile("(?<=bookPages = Number\\(\\\")\\d+(?=\\\"\\))");

		for (Element script : scripts) {
			html = script.html();
			if (html.contains("bookPages")) {
				html = html.replace("\r\n", "");

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
		String url = String.format(SHARE_URL, detail.episodeId);
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
		return DETAIL_URL + id;
	}
}
