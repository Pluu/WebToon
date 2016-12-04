package com.pluu.support.naver;

import android.content.Context;
import android.text.TextUtils;

import com.pluu.support.impl.AbstractDetailApi;
import com.pluu.webtoon.item.Detail;
import com.pluu.webtoon.item.DetailView;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.ShareItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pluu.webtoon.item.ERROR_TYPE.NOT_SUPPORT;

/**
 * 네이버 웹툰 상세 API
 * Created by PLUUSYSTEM-NEW on 2015-10-29.
 */
public class NaverDetailApi extends AbstractDetailApi {

	private final String SHARE_URL = "http://m.comic.naver.com/webtoon/detail.nhn?titleId=%s&no=%s";
	private final List<String> SKIP_DETAIL = new ArrayList<String>() {{
		add("http://static.naver.com/m/comic/im/txt_ads.png");
		add("http://static.naver.com/m/comic/im/toon_app_pop.png");
	}};
	private final Pattern ID_PATTERN = Pattern.compile("(?<=no=)\\d+");
	private final Pattern ID_PATTERN2 = Pattern.compile("(?<=')\\d+(?='\\);$)");

	private String webToonId, episodeId;

	public NaverDetailApi(Context context) {
		super(context);
	}

	@Override
	public Detail parseDetail(Episode episode) {
		this.webToonId = episode.getToonId();
		this.episodeId = episode.getEpisodeId();

		Detail ret = new Detail();
		ret.webtoonId = webToonId;
		ret.episodeId = episodeId;

		String response;
		try {
			response = requestApi();
		} catch (Exception e) {
			e.printStackTrace();
			ret.list = new ArrayList<>();
			return ret;
		}

		Document doc = Jsoup.parse(response);
		ret.title = doc.select("div[class=chh] span, h1[class=tit]").first().text();

		Elements tempElements = doc.select("a[class=prev smt-btn-prev-cut]");
		if (tempElements != null && !tempElements.isEmpty()) {
			parseSmartToon(ret, doc);
			return ret;
		}

		tempElements = doc.select("div[class=viewer cuttoon]");
		if (tempElements != null && !tempElements.isEmpty()) {
			// 컷툰
			parseCutToon(ret, doc);
			return ret;
		}

		tempElements = doc.select(".oz-loader");
		if (tempElements != null && !tempElements.isEmpty()) {
			// osLoader
			ret.errorType = NOT_SUPPORT;
			return ret;
		}

		// 일반 웹툰
		parseNormal(ret, doc);

		return ret;
	}

	private void parseNormal(Detail ret, Document doc) {
		ret.list = parseDetailNormalType(doc);

		// 이전, 다음화
		for (Element element : doc.select("div[class=sc2] a")) {
            if (!element.select("span[class=nx]").isEmpty()) {
                ret.nextLink = getNormalPrevNextIdx(element.attr("onclick"));
            } else if (!element.select("span[class=pv]").isEmpty()) {
                ret.prevLink = getNormalPrevNextIdx(element.attr("onclick"));
            }
        }
	}

	private String getNormalPrevNextIdx(String str) {
		Matcher matcher = ID_PATTERN2.matcher(str);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	private void parseCutToon(Detail ret, Document doc) {
		ret.list = parseDetailCutToonType(doc);

		// 이전, 다음화
		Elements navi = doc.select(".paging_wrap");
		Elements temp = navi.select("a[class=pg_next]");
		if (temp != null && !temp.isEmpty()) {
            ret.nextLink = getEpisodeId(temp.first().attr("href"));
        }
		temp = navi.select("a[class=pg_prev]");
		if (temp != null && !temp.isEmpty()) {
            ret.prevLink = getEpisodeId(temp.first().attr("href"));
        }
	}

	private void parseSmartToon(Detail ret, Document doc) {
		// 스마트툰
		ret.list = parseDetailSmartToonType(doc);

		// 이전, 다음화
		Elements navi = doc.select(".viewer_inner");
		Elements temp = navi.select("a[class=btn_next]");
		if (temp != null && !temp.isEmpty()) {
            ret.nextLink = getEpisodeId(temp.first().attr("href"));
        }
		temp = navi.select("a[class=btn_prev]");
		if (temp != null && !temp.isEmpty()) {
            ret.prevLink = getEpisodeId(temp.first().attr("href"));
        }
	}

	private String getEpisodeId(String str) {
		Matcher matcher = ID_PATTERN.matcher(str);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	private List<DetailView> parseDetailCutToonType(Document doc) {
		List<DetailView> list = new ArrayList<>();

		Pattern pattern = Pattern.compile("(?<=sImageUrl :')https?://(\\w*:\\w*@)?[-\\w.]+(:\\d+)?(/([\\w/_.]*(\\?\\S+)?)?)?(?=', sWatermarkUrl)");
		Elements select = doc.select("script[type=text/javascript]");
		for (Element element : select) {
			Matcher matcher = pattern.matcher(element.html().replace("\r\n", ""));
			while (matcher.find()) {
				list.add(DetailView.createImage(matcher.group()));
			}
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
		return String.format(SHARE_URL, webToonId, episodeId);
	}
}
