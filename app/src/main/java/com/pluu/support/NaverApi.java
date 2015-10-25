package com.pluu.support;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pluu.webtoon.BaseActivity;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.DetailView;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;
import com.pluu.webtoon.api.Status;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;
import com.pluu.webtoon.common.Const;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 네이버 웹툰 Parse Api
 * Created by nohhs on 2015-03-12.
 */
public class NaverApi extends BaseApiImpl {
	private final String[] value = {"mon", "tue", "wed", "thu", "fri", "sat", "sun", "fin"};
	private final String HOST_URL = "http://m.comic.naver.com/webtoon/weekday.nhn";
	private final List<String> SKIP_DETAIL = new ArrayList<String>() {{
		add("http://static.naver.com/m/comic/im/txt_ads.png");
		add("http://static.naver.com/m/comic/im/toon_app_pop.png");
	}};

	public NaverApi() {
		super(new String[]{"월", "화", "수", "목", "금", "토", "일", "완결"});
	}

	@Override
	public BaseActivity.NAV_ITEM getNaviItem() {
		return BaseActivity.NAV_ITEM.NAVER;
	}

	@Override
	public int getMainTitleColor(Context context) {
		return context.getResources().getColor(R.color.naver_color);
	}

	@Override
	public String getWeeklyUrl(int position) {
		return HOST_URL + "?week=" + value[position];
	}

	@Override
	public List<WebToonInfo> parseMain(Context context, String url, int position) {
		ArrayList<WebToonInfo> list = new ArrayList<>();
		try {
			Document doc = getConnection(url).get();
			Elements links = doc.select("#pageList a");
			WebToonInfo item = null;
			Pattern pattern = Pattern.compile("(?<=titleId=)\\d+");
			for (Element a : links) {
				Matcher matcher = pattern.matcher(a.attr("href"));
				if (!matcher.find()) {
					continue;
				}

				item = new WebToonInfo(matcher.group());
				item.setUrl(a.absUrl("href"));
				item.setTitle(a.select(".toon_name").text());
				item.setImage(a.select("img").first().attr("src"));

				if (!a.select(".aside_info .ico_up").isEmpty()) {
					// 최근 업데이트
					item.setStatus(Status.UPDATE);
				} else if (!a.select(".aside_info .ico_break").isEmpty()) {
					// 휴재
					item.setStatus(Status.BREAK);
				}
				item.setWriter(a.select(".sub_info").text());
				item.setRate(Const.getRateNameByRate(a.select("span[class=if1 st_r]").text()));
				item.setUpdateDate(a.select("span[class=if1]").text());
				list.add(item);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public WebToon parseEpisode(Context context, WebToonInfo info, String url) {
		WebToon webToon = new WebToon(this, url);
		try {
			Document doc = getConnection(url).get();
			webToon.episodes = parseList(info, url, doc);
			webToon.nextLink = parsePage(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return webToon;
	}

	private List<Episode> parseList(WebToonInfo info, String hostUrl, Document doc) {
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
//		episode.setUpdateDate(info.select("span[class=if1]").text());

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
	public Detail parseDetail(Context context, Episode episode, String url) {
		Detail ret = new Detail();
		ret.webtoonId = episode.getWebtoonId();

		try {
			Pattern pattern = Pattern.compile("no=\\d+");

			Document doc = getConnection(url).get();
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

		} catch (IOException e) {
			e.printStackTrace();
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
	public ShareItem getDetailShare(Episode episode, Detail detail) {
		String url = String.format("http://m.comic.naver.com/webtoon/detail.nhn?titleId=%s&no=%s",
								   detail.webtoonId, detail.episodeId);
		ShareItem item = new ShareItem();
		item.title = episode.getTitle() + " / " + detail.title;
		item.url = url;
		return item;
	}
}
