package com.pluu.support.kakao;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pluu.support.BaseApiImpl;
import com.pluu.webtoon.ui.BaseActivity;
import com.pluu.webtoon.R;
import com.pluu.webtoon.api.Detail;
import com.pluu.webtoon.api.DetailView;
import com.pluu.webtoon.api.Episode;
import com.pluu.webtoon.api.ShareItem;
import com.pluu.webtoon.api.Status;
import com.pluu.webtoon.api.WebToon;
import com.pluu.webtoon.api.WebToonInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 카카오 페이지 만화 Parse Api
 * Created by nohhs on 2015-04-07.
 */
public class KakaoPageApi extends BaseApiImpl {
	private final String WEEKLY_URL = "http://page.kakao.com/main/ajax_weekly_web?week=%d&categoryType=MC09&navi=1&inkatalk=0&categoryUid=10&subCategoryUid=0";
	private final String FIRST_EPISODE= "http://page.kakao.com/home/%s?categoryUid=10&subCategoryUid=0&navi=1&inkatalk=0";
	private final String MORE_EPISODE = "http://page.kakao.com/home/singlelist?seriesId=%s&offset=%d&navi=1&inkatalk=0";
	private final String DETAIL_URL = "http://page.kakao.com/viewer?productId=%s&categoryUid=10&subCategoryUid=0";

	private final int SIZE = 25;
	private int offset;

	private Pattern pattern = Pattern.compile("(?<=productId=)\\d+");

	private Episode firstEpisode;

	public KakaoPageApi() {
		super(new String[]{"월", "화", "수", "목", "금", "토", "일"});
	}

	@Override
	public BaseActivity.NAV_ITEM getNaviItem() {
		return BaseActivity.NAV_ITEM.KAKAOPAGE;
	}

	@Override
	public int getMainTitleColor(Context context) {
		return context.getResources().getColor(R.color.kakao_color);
	}

	@Override
	public String getWeeklyUrl(int position) {
		return String.format(WEEKLY_URL, position + 1);
	}

	@Override
	public List<WebToonInfo> parseMain(Context context, String url, int position) {
		ArrayList<WebToonInfo> list = new ArrayList<>();
		try {
			Document doc = getConnection(url).get();
			Elements links = doc.select(".l_link");
			WebToonInfo item = null;
			Pattern pattern = Pattern.compile("(?<=/home/)\\d+");
			String href;

			String updateStatus = "up";
			for (Element a : links) {
				href = a.absUrl("data-href");
				Matcher matcher = pattern.matcher(href);
				if (!matcher.find()) {
					continue;
				}

				item = new WebToonInfo(matcher.group());
				item.setUrl(href);
				item.setTitle(a.select(".title").first().text());
				item.setImage(a.select(".listImg").first().attr("src"));

				if (updateStatus.equals(a.select(".badgeImg").attr("alt"))) {
					item.setStatus(Status.UPDATE);
				}
				item.setWriter(a.select("span[class=info elInfo ellipsis]").text());
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
			Document doc;

			if (offset > 0) {
				doc = getConnection(String.format(MORE_EPISODE, info.getWebtoonId(), offset)).get();
			} else {
				doc = getConnection(String.format(FIRST_EPISODE, info.getWebtoonId())).get();
				String firstUrl = doc.select("span[class=firstItemViewBtn]").first().absUrl("data-href");
				Matcher matcher = pattern.matcher(firstUrl);
				if (matcher.find()) {
					firstEpisode = new Episode(info, matcher.group());
					firstEpisode.setUrl(firstUrl);
				}
			}

			webToon.episodes = parseList(info, url, doc);
			if (!webToon.episodes.isEmpty()) {
				webToon.nextLink = parsePage(info);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return webToon;
	}

	private List<Episode> parseList(WebToonInfo info, String url, Document doc) {
		List<Episode> list = new ArrayList<>();
		Elements links = doc.select("li[class=list viewerList]");
		Episode item;

		try {
			for (Element a : links) {
				item = new Episode(info, a.select(".productId").attr("value"));
				item.setUrl(String.format(DETAIL_URL, item.getEpisodeId()));
				item.setImage(a.select(".thum").attr("src"));
				item.setEpisodeTitle(a.select("span[class=title ellipsis_hd]").text());
				item.setUpdateDate(a.select(".date").text());
				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	private String parsePage(WebToonInfo info) {
		offset += SIZE;
		return info.getUrl();
	}

	@Override
	public Detail parseDetail(Context context, Episode episode, String url) {
		Detail ret = new Detail();
		ret.webtoonId = episode.getWebtoonId();
		List<DetailView> list = new ArrayList<>();

		try {
			Document doc = getConnection(url).get();

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

			Elements elements = doc.select(".targetImg");
			for (Element img : elements) {
				list.add(DetailView.createImage(img.absUrl("data-original")));
			}
			elements = doc.select(".viewWrp li input");
			for (Element img : elements) {
				list.add(DetailView.createImage(img.absUrl("value")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ret.list = list;
		return ret;
	}

	@Override
	public Episode getFirstEpisode(Episode item) {
		return firstEpisode;
	}

	@Override
	public ShareItem getDetailShare(Episode episode, Detail detail) {
		ShareItem item = new ShareItem();
		item.title = episode.getTitle() + " / " + detail.title;
		item.url = episode.getUrl();
		return item;
	}

	@Override
	public void refreshEpisode() {
		offset = 0;
	}

}
